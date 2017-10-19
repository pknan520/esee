package com.nong.nongo2o.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.nong.nongo2o.AdventurerApp;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.response.WxAccessToken;
import com.nong.nongo2o.entity.response.WxInfo;
import com.nong.nongo2o.module.login.viewModel.LoginVM;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.service.InitDataService;
import com.nong.nongo2o.uils.Constant;
import com.nong.nongo2o.uils.SPUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.R.attr.action;
import static android.R.attr.fragment;

/**
 * Created by Administrator on 2017-9-22.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdventurerApp.wxApi.handleIntent(getIntent(), this);
    }

    /**
     * 登录微信
     *
     * @param api 微信服务api
     */
    public static void loginWeixin(Context context, IWXAPI api) {
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context.getApplicationContext(), "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        api.sendReq(req);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                // 获取code
                String code = ((SendAuth.Resp) baseResp).code;
                // 通过code获取授权口令access_token
//                LoginVM.loginToServer(this, code);
                login(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "您已取消授权", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "您已拒绝授权", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "未知错误", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void login(String code) {
        final Intent intent = new Intent();

        RetrofitHelper.getWxServiceAPI()
                .getWxAccessToken(Constant.WX_APP_ID, Constant.WX_APP_SECRET, code, "authorization_code")
                .subscribeOn(Schedulers.io())
                .flatMap(wxAccessToken -> {
                    intent.putExtra("wxAccessToken", wxAccessToken);
                    return RetrofitHelper.getWxServiceAPI().getWxInfo(wxAccessToken.getAccess_token(), wxAccessToken.getOpenid());
                })
                .flatMap(wxInfo -> {
                    intent.putExtra("wxInfo", wxInfo);
                    return RetrofitHelper.getAccountAPI().login(wxInfo.getOpenid());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResp -> {
                    if (userResp.getCode().equals("0")) {
                        UserInfo.setOurInstance(new UserInfo(userResp.getData()));

                        SPUtils.put(this, "WX_ACCESS_TOKEN", ((WxAccessToken) intent.getSerializableExtra("wxAccessToken")).getAccess_token());
                        SPUtils.put(this, "WX_REFRESH_TOKEN", ((WxAccessToken) intent.getSerializableExtra("wxAccessToken")).getRefresh_token());
                        SPUtils.put(this, "WX_ACCESS_EXPIRE", ((WxAccessToken) intent.getSerializableExtra("wxAccessToken")).getExpires_in() * 1000 + Calendar.getInstance().getTimeInMillis());
                        SPUtils.put(this, "WX_REFRESH_EXPIRE", 30 * 24 * 60 * 1000 + Calendar.getInstance().getTimeInMillis());
                        SPUtils.put(this, "WX_OPENID", ((WxAccessToken) intent.getSerializableExtra("wxAccessToken")).getOpenid());

                        intent.setAction("loginSuccess");
                        finish();
                    } else {
                        intent.setAction("bindMobile");
                        finish();
                    }
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }, throwable -> {
                    intent.setAction("loginFail");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}

package com.nong.nongo2o.module.welcome.viewModel;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.module.welcome.WelcomeActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.service.InitDataService;
import com.nong.nongo2o.uils.Constant;
import com.nong.nongo2o.uils.SPUtils;
import com.nong.nongo2o.uils.WxUtils;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-10-9.
 */

public class WelcomeVM implements ViewModel {

    private WelcomeActivity activity;

    public WelcomeVM(WelcomeActivity activity) {
        this.activity = activity;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if ((boolean) SPUtils.get(activity, "IS_APP_FIRST_OPEN", false)) {

            // TODO: 2017-10-9 第一次打开app,此处应跳转引导页，暂时跳至登录页
            activity.startActivity(LoginActivity.newIntent(activity, false));
            activity.overridePendingTransition(R.anim.anim_right_in, 0);
            activity.finish();
        } else {
            if (SPUtils.contains(activity, "WX_ACCESS_TOKEN") && SPUtils.contains(activity, "WX_REFRESH_TOKEN") && SPUtils.contains(activity, "WX_OPENID")
                    && SPUtils.contains(activity, "WX_ACCESS_EXPIRE") && SPUtils.contains(activity, "WX_REFRESH_EXPIRE")) {

                //  有登陆信息
                if (WxUtils.isExpireAccess(activity)) {

                    //  accessToken过期
                    if (WxUtils.isExpireRefresh(activity)) {

                        //  refreshToken过期，需要重新授权
                        activity.startActivity(LoginActivity.newIntent(activity, false));
                        activity.overridePendingTransition(R.anim.anim_right_in, 0);
                    } else {

                        //  刷新accessToken
                        RetrofitHelper.getWxServiceAPI()
                                .refreshAccessToken(Constant.WX_APP_ID, "refresh_token", (String) SPUtils.get(activity, "WX_REFRESH_TOKEN", ""))
                                .subscribeOn(Schedulers.io())
                                .flatMap(wxAccessToken -> {
                                    SPUtils.put(activity, "WX_ACCESS_TOKEN", wxAccessToken.getAccess_token());
                                    SPUtils.put(activity, "WX_REFRESH_TOKEN", wxAccessToken.getRefresh_token());
                                    SPUtils.put(activity, "WX_ACCESS_EXPIRE", wxAccessToken.getExpires_in() * 1000 + Calendar.getInstance().getTimeInMillis());
                                    SPUtils.put(activity, "WX_REFRESH_EXPIRE", 30 * 24 * 60 * 1000 + Calendar.getInstance().getTimeInMillis());
                                    return RetrofitHelper.getWxServiceAPI().getWxInfo(wxAccessToken.getAccess_token(), wxAccessToken.getOpenid());
                                })
                                .flatMap(wxInfo -> {
                                    SPUtils.put(activity, "WX_OPENID", wxInfo.getOpenid());
                                    return RetrofitHelper.getAccountAPI().login(wxInfo.getOpenid());
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new ApiResponseFunc<>())
                                .subscribe(this::toMainActivity, throwable -> Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                } else {

                    //  获取微信个人信息
                    RetrofitHelper.getWxServiceAPI()
                            .getWxInfo((String) SPUtils.get(activity, "WX_ACCESS_TOKEN", ""), (String) SPUtils.get(activity, "WX_OPENID", ""))
                            .subscribeOn(Schedulers.io())
                            .flatMap(wxInfo -> {
                                SPUtils.put(activity, "WX_OPENID", wxInfo.getOpenid());
                                return RetrofitHelper.getAccountAPI().login(wxInfo.getOpenid());
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new ApiResponseFunc<>())
                            .subscribe(this::toMainActivity, throwable -> Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show());
                }
            } else {

                //  无登录信息或登录信息不全
                activity.startActivity(LoginActivity.newIntent(activity, false));
                activity.overridePendingTransition(R.anim.anim_right_in, 0);
                activity.finish();
            }
        }
    }

    private void toMainActivity(UserInfo user) {
        UserInfo.setOurInstance(new UserInfo(user));

        Intent intent = new Intent("loginSuccess");
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        activity.startService(new Intent(activity, InitDataService.class));

        activity.startActivity(MainActivity.newIntent(activity));
        activity.finish();
    }

}

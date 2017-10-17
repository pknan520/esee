package com.nong.nongo2o.module.welcome;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.databinding.ActivityWelcomeBinding;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.module.welcome.viewModel.WelcomeVM;
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

public class WelcomeActivity extends RxBaseActivity {

    private static final int sleepTime = 2000;

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        binding.rootView.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(() -> {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //  暂时没有引导页
            if ((boolean) SPUtils.get(this, "IS_APP_FIRST_OPEN", false)) {

                // 第一次打开app,此处应跳转引导页
                this.startActivity(GuideActivity.newIntent(this));
                this.finish();
                this.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
            } else {
                if (SPUtils.contains(this, "WX_ACCESS_TOKEN") && SPUtils.contains(this, "WX_REFRESH_TOKEN") && SPUtils.contains(this, "WX_OPENID")
                        && SPUtils.contains(this, "WX_ACCESS_EXPIRE") && SPUtils.contains(this, "WX_REFRESH_EXPIRE")) {

                    //  有登陆信息
                    if (WxUtils.isExpireAccess(this)) {

                        //  accessToken过期
                        if (WxUtils.isExpireRefresh(this)) {

                            //  refreshToken过期，需要重新授权
                            toLogin();
                        } else {

                            //  刷新accessToken
                            RetrofitHelper.getWxServiceAPI()
                                    .refreshAccessToken(Constant.WX_APP_ID, "refresh_token", (String) SPUtils.get(this, "WX_REFRESH_TOKEN", ""))
                                    .subscribeOn(Schedulers.io())
                                    .flatMap(wxAccessToken -> {
                                        SPUtils.put(this, "WX_ACCESS_TOKEN", wxAccessToken.getAccess_token());
                                        SPUtils.put(this, "WX_REFRESH_TOKEN", wxAccessToken.getRefresh_token());
                                        SPUtils.put(this, "WX_ACCESS_EXPIRE", wxAccessToken.getExpires_in() * 1000 + Calendar.getInstance().getTimeInMillis());
                                        SPUtils.put(this, "WX_REFRESH_EXPIRE", 30 * 24 * 60 * 1000 + Calendar.getInstance().getTimeInMillis());
                                        return RetrofitHelper.getWxServiceAPI().getWxInfo(wxAccessToken.getAccess_token(), wxAccessToken.getOpenid());
                                    })
                                    .flatMap(wxInfo -> {
                                        SPUtils.put(this, "WX_OPENID", wxInfo.getOpenid());
                                        return RetrofitHelper.getAccountAPI().login(wxInfo.getOpenid());
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .map(new ApiResponseFunc<>())
                                    .subscribe(this::toMainActivity, throwable -> {
                                        toLogin();
                                        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {

                        //  获取微信个人信息
                        RetrofitHelper.getWxServiceAPI()
                                .getWxInfo((String) SPUtils.get(this, "WX_ACCESS_TOKEN", ""), (String) SPUtils.get(this, "WX_OPENID", ""))
                                .subscribeOn(Schedulers.io())
                                .flatMap(wxInfo -> {
                                    SPUtils.put(this, "WX_OPENID", wxInfo.getOpenid());
                                    return RetrofitHelper.getAccountAPI().login(wxInfo.getOpenid());
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(new ApiResponseFunc<>())
                                .subscribe(this::toMainActivity, throwable -> {
                                    toLogin();
                                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {

                    //  无登录信息或登录信息不全
                    toLogin();
                }
            }
        }).start();
    }

    private void toMainActivity(UserInfo user) {
        UserInfo.setOurInstance(new UserInfo(user));

        Intent intent = new Intent("loginSuccess");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        startService(new Intent(this, InitDataService.class));

        startActivity(MainActivity.newIntent(this));
        finish();
    }

    private void toLogin() {
        startActivity(LoginActivity.newIntent(this, false));
        finish();
        overridePendingTransition(R.anim.anim_right_in, 0);
    }
}

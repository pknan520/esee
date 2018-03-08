package com.nong.nongo2o.module.welcome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.nong.nongo2o.service.VersionUpdateHelper;
import com.nong.nongo2o.uils.Constant;
import com.nong.nongo2o.uils.SPUtils;
import com.nong.nongo2o.uils.WxUtils;
import com.nong.nongo2o.uils.dbUtils.DbUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-10-9.
 */

public class WelcomeActivity extends RxBaseActivity {

    private static final int sleepTime = 2000;
    //  权限数组
    private String[] permissions = new String[] {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private List<String> permissionList = new ArrayList<>();
    private boolean mShowRequestPermission = true;//用户是否禁止权限

    private ActivityWelcomeBinding binding;
    private VersionUpdateHelper helper;

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

        checkPermission();
    }

    private void checkPermission() {
        //  判断哪些权限未授予
        new RxPermissions(this)
                .request(permissions)
                .subscribe(granted -> {
                    if (granted) {
                        //  复制数据表
                        if (!SPUtils.contains(getApplicationContext(), "city_database") || !(boolean) SPUtils.get(getApplicationContext(), "city_database", false)) {
                            DbUtils.copyDBToDatabases(getApplicationContext());
                        }

                        helper = new VersionUpdateHelper(this);
                        VersionUpdateHelper.resetCancelFlag();
                        helper.setCheckCallBack(code -> {
                            if (code != VersionUpdateHelper.MUST_UPDATE) {
                                initData();
                            }
                        });
                        helper.startUpdateVersion();
                    } else {
                        Toast.makeText(this, "拒绝权限将导致部分功能无法正常使用", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initData() {
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
                                        return RetrofitHelper.getAccountAPI().login(wxInfo.getOpenid(), wxInfo.getHeadimgurl(), wxInfo.getNickname());
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
                                    return RetrofitHelper.getAccountAPI().login(wxInfo.getOpenid(), wxInfo.getHeadimgurl(), wxInfo.getNickname());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (showRequestPermission) {
                            //  重新申请权限
                            checkPermission();
                            return;
                        } else {
                            //  已经禁止
                            mShowRequestPermission = false;
                        }
                    }
                }
                // TODO: 2017-8-23 权限授予完毕
                Toast.makeText(this, "权限都授予完毕", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

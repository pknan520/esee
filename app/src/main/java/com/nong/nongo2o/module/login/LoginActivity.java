package com.nong.nongo2o.module.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.databinding.ActivityLoginBinding;
import com.nong.nongo2o.module.login.fragment.LoginFragment;
import com.nong.nongo2o.uils.SPUtils;
import com.nong.nongo2o.uils.dbUtils.DbUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017-8-4.
 */

public class LoginActivity extends RxBaseActivity {

    private ActivityLoginBinding binding;
    //  权限数组
    private String[] permissions = new String[] {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private List<String> permissionList = new ArrayList<>();
    private boolean mShowRequestPermission = true;//用户是否禁止权限

    public static Intent newIntent(Context context, boolean canClose) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("canClose", canClose);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        checkPermission();
        initView();
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
                    } else {
                        Toast.makeText(this, "拒绝权限将导致部分功能无法正常使用", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initView() {
        replaceFragment(R.id.fl, LoginFragment.newInstance(getIntent().getBooleanExtra("canClose", false)), LoginFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
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

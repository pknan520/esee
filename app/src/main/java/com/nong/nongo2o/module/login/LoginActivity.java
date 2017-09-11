package com.nong.nongo2o.module.login;

import android.Manifest;
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

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        checkPermission();
        initView();
    }

    private void checkPermission() {
        //  判断哪些权限未授予
        permissionList.clear();
        for (String permissionStr : permissions) {
            if (ContextCompat.checkSelfPermission(this, permissionStr) != PackageManager.PERMISSION_GRANTED) {
                //  未授予的添加到permissionList中
                permissionList.add(permissionStr);
            }
        }

        //  判断permissionList是否为空
        if (permissionList.isEmpty()) {
            //  列表为空，表示所需权限都授予了
            // TODO: 2017-8-23 权限授予完毕
            Toast.makeText(this, "权限都授予完毕", Toast.LENGTH_SHORT).show();
        } else {
            //  请求权限
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 100);
        }

    }

    private void initView() {
        replaceFragment(R.id.fl, LoginFragment.newInstance(), LoginFragment.TAG);
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

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

    public static Intent newIntent(Context context, boolean canClose) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("canClose", canClose);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        initView();
    }

    private void initView() {
        replaceFragment(R.id.fl, LoginFragment.newInstance(getIntent().getBooleanExtra("canClose", false)), LoginFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

package com.nong.nongo2o.module.personal.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityPersonalSettingBinding;
import com.nong.nongo2o.module.personal.fragment.SettingFragment;
import com.nong.nongo2o.module.personal.viewModel.SettingVM;

/**
 * Created by Administrator on 2017-6-26.
 */

public class SettingActivity extends RxBaseToolbarActivity {

    private ActivityPersonalSettingBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_setting);
        return binding;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        replaceFragment(R.id.fl, SettingFragment.newInstance(), SettingFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }

    public void setToolbarTitle(String title) {
        setTitle("");
        binding.tvToolbarTitle.setText(title);
    }
}

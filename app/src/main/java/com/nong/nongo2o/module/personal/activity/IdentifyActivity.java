package com.nong.nongo2o.module.personal.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityIdentifyBinding;
import com.nong.nongo2o.module.personal.viewModel.IdentifyVM;

/**
 * Created by Administrator on 2017-6-30.
 */

public class IdentifyActivity extends RxBaseToolbarActivity {

    private ActivityIdentifyBinding binding;
    private IdentifyVM vm;

    public static Intent newIntent(Context context) {
        return new Intent(context, IdentifyActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_identify);
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
        vm = new IdentifyVM(this);
        binding.setViewModel(vm);
    }

    private void initView() {
        setTitle("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

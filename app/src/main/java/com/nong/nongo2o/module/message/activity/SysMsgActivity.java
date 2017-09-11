package com.nong.nongo2o.module.message.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivitySysMsgBinding;
import com.nong.nongo2o.module.message.fragment.SysMsgListFragment;

/**
 * Created by Administrator on 2017-7-19.
 */

public class SysMsgActivity extends RxBaseToolbarActivity {

    private ActivitySysMsgBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, SysMsgActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
         binding = DataBindingUtil.setContentView(this, R.layout.activity_sys_msg);
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
        replaceFragment(R.id.fl, SysMsgListFragment.newInstance(), SysMsgListFragment.TAG);
    }

    public void setToolbarTitle(String title) {
        setTitle("");
        binding.tvToolbarTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

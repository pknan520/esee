package com.nong.nongo2o.module.personal.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityGoodsManagerBinding;
import com.nong.nongo2o.module.personal.fragment.GoodsManagerTotalFragment;

/**
 * Created by Administrator on 2017-6-26.
 */

public class GoodsManagerActivity extends RxBaseToolbarActivity {

    private ActivityGoodsManagerBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, GoodsManagerActivity.class);
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_manager);
        return binding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setTitle("");
        replaceFragment(R.id.fl, GoodsManagerTotalFragment.newInstance(), GoodsManagerTotalFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }

    public void setToolbarTitle(String title) {
        setTitle("");
        binding.tvToolbarTitle.setText(title);

        binding.tabToolbar.setVisibility(View.GONE);
        binding.tvToolbarTitle.setVisibility(View.VISIBLE);
    }

    public TabLayout getTabLayout() {
        binding.tabToolbar.setVisibility(View.VISIBLE);
        binding.tvToolbarTitle.setVisibility(View.GONE);

        return binding.tabToolbar;
    }
}

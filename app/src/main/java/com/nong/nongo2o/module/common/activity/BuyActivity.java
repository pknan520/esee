package com.nong.nongo2o.module.common.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityBuyBinding;
import com.nong.nongo2o.module.common.fragment.CreateOrderFragment;

/**
 * Created by Administrator on 2017-7-17.
 */

public class BuyActivity extends RxBaseToolbarActivity {

    private ActivityBuyBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, BuyActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buy);
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
        replaceFragment(R.id.fl, CreateOrderFragment.newInstance(), CreateOrderFragment.TAG);
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

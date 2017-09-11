package com.nong.nongo2o.module.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityMerchantGoodsBinding;
import com.nong.nongo2o.module.merchant.fragment.MerchantGoodsFragment;

/**
 * Created by Administrator on 2017-7-4.
 */

public class MerchantGoodsActivity extends RxBaseToolbarActivity {

    private ActivityMerchantGoodsBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, MerchantGoodsActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_merchant_goods);
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
        binding.tvToolbarTitle.setText("商品标题");
        replaceFragment(R.id.fl, MerchantGoodsFragment.newInstance(), MerchantGoodsFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
//
//    public TabLayout getTabLayout() {
//        return binding.tab;
//    }
}

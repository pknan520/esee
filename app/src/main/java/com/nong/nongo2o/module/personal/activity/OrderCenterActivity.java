package com.nong.nongo2o.module.personal.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityOrderCenterBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;
import com.nong.nongo2o.module.personal.fragment.OrderListTotalFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderCenterActivity extends RxBaseToolbarActivity {

    private ActivityOrderCenterBinding binding;
    private boolean isMerchantMode;

    public static Intent newIntent(Context context, Order order, boolean isMerchantMode) {
        Intent intent = new Intent(context, OrderCenterActivity.class);
        intent.putExtra("order", order);
        intent.putExtra("isMerchantMode", isMerchantMode);
        return intent;
    }

    public static Intent newIntent(Context context, boolean isMerchantMode, int pos) {
        Intent intent = new Intent(context, OrderCenterActivity.class);
        intent.putExtra("isMerchantMode", isMerchantMode);
        intent.putExtra("pos", pos);
        return intent;
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_center);
        return binding;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isMerchantMode = getIntent().getBooleanExtra("isMerchantMode", false);
        initView();
    }

    private void initView() {
        if (getIntent().getSerializableExtra("order") != null) {
            replaceFragment(R.id.fl, OrderDetailFragment.newInstance((Order) getIntent().getSerializableExtra("order"), isMerchantMode), OrderDetailFragment.TAG);
        } else {
            replaceFragment(R.id.fl, OrderListTotalFragment.newInstance(isMerchantMode, getIntent().getIntExtra("pos", 0)), OrderListTotalFragment.TAG);
        }
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

package com.nong.nongo2o.module.common.buy.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityBuyBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.module.common.buy.fragment.CreateOrderFragment;
import com.nong.nongo2o.module.common.buy.fragment.PayFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-7-17.
 */

public class BuyActivity extends RxBaseToolbarActivity {

    private ActivityBuyBinding binding;

    public static Intent newIntent(Context context, @Nullable ArrayList<OrderDetail> orderDetails, @Nullable Order order, boolean pay) {
        Intent intent = new Intent(context, BuyActivity.class);
        if (orderDetails != null) intent.putExtra("orderDetails", orderDetails);
        if (order != null) intent.putExtra("order", order);
        intent.putExtra("pay", pay);
        return intent;
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
        if (getIntent().getBooleanExtra("pay", false)) {
            replaceFragment(R.id.fl, PayFragment.newInstance((Order) getIntent().getSerializableExtra("order")), PayFragment.TAG);
        } else {
            replaceFragment(R.id.fl, CreateOrderFragment.newInstance((ArrayList<OrderDetail>) getIntent().getSerializableExtra("orderDetails")), CreateOrderFragment.TAG);
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

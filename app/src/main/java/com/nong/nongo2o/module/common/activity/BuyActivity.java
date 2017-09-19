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
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.module.common.fragment.CreateOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-7-17.
 */

public class BuyActivity extends RxBaseToolbarActivity {

    private ActivityBuyBinding binding;

    // TODO: 2017-9-18 临时容错，以后删除
    public static Intent newIntent(Context context) {
        return new Intent(context, BuyActivity.class);
    }

    public static Intent newIntent(Context context, ArrayList<OrderDetail> orderDetails) {
        Intent intent = new Intent(context, BuyActivity.class);
        intent.putExtra("orderDetails", orderDetails);
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
        replaceFragment(R.id.fl, CreateOrderFragment.newInstance((ArrayList<OrderDetail>) getIntent().getSerializableExtra("orderDetails")), CreateOrderFragment.TAG);
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

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
import com.nong.nongo2o.module.personal.fragment.OrderListTotalFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderCenterActivity extends RxBaseToolbarActivity {

    private ActivityOrderCenterBinding binding;
    private boolean isMerchantMode;
    public static Intent newIntent(Context context,boolean isMerchantMode, int pos) {
        Intent intent = new Intent(context, OrderCenterActivity.class);
        intent.putExtra("isMerchantMode",isMerchantMode);
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
        isMerchantMode = getIntent().getBooleanExtra("isMerchantMode",false);
        initView();
    }

    private void initView() {
        replaceFragment(R.id.fl, OrderListTotalFragment.newInstance(isMerchantMode, getIntent().getIntExtra("pos", 0)), OrderListTotalFragment.TAG);
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

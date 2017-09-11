package com.nong.nongo2o.module.share.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivitySharePublishBinding;

/**
 * Created by Administrator on 2017-6-23.
 */

public class SharePublishActivity extends RxBaseToolbarActivity {

    public static final String TAG = "SharePublishActivity";

    private ActivitySharePublishBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, SharePublishActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_publish);
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_bottom_out);
    }
}

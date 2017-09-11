package com.nong.nongo2o.module.share.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityShareDetailBinding;
import com.nong.nongo2o.module.share.fragment.ShareDetailFragment;

/**
 * Created by Administrator on 2017-7-3.
 */

public class ShareDetailActivity extends RxBaseToolbarActivity {

    private ActivityShareDetailBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, ShareDetailActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_detail);
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
        replaceFragment(R.id.fl, ShareDetailFragment.newInstance(), ShareDetailFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

package com.nong.nongo2o.module.dynamic.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityDynamicPublishBinding;
import com.nong.nongo2o.entities.response.DynamicDetail;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.dynamic.fragment.DynamicPublishFragment;

/**
 * Created by Administrator on 2017-7-1.
 */

public class DynamicPublishActivity extends RxBaseToolbarActivity {

    private ActivityDynamicPublishBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, DynamicPublishActivity.class);
    }

    public static Intent newIntent(Context context, Moment dynamic) {
        Intent intent = new Intent(context, DynamicPublishActivity.class);
        intent.putExtra("dynamic", (Parcelable) dynamic);
        return intent;
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dynamic_publish);
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
        if (getIntent().getParcelableExtra("dynamic") != null) {
            replaceFragment(R.id.fl, DynamicPublishFragment.newInstance(getIntent().getParcelableExtra("dynamic")), DynamicPublishFragment.TAG);
        } else {
            replaceFragment(R.id.fl, DynamicPublishFragment.newInstance(), DynamicPublishFragment.TAG);
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

    public TabLayout getTabLayout() {
        return binding.tabToolbar;
    }

}

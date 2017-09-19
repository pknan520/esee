package com.nong.nongo2o.module.common.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivitySelectAreaBinding;
import com.nong.nongo2o.module.common.fragment.SelectAreaListFragment;

/**
 * Created by Administrator on 2017-8-21.
 */

public class SelectAreaActivity extends RxBaseToolbarActivity {

    public static final int AREA_PROVINCE = 0, AREA_CITY = 1, AREA_DISTRICT = 2, AREA_STREET = 3;

    private ActivitySelectAreaBinding binding;
    private SelectAreaListFragment fragment;

    public static Intent newIntent(Context context) {
        return new Intent(context, SelectAreaActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_area);
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
        setToolbarTitle("选择地区");

        fragment = SelectAreaListFragment.newInstance(AREA_PROVINCE, null, null, null);
        replaceFragment(R.id.fl, fragment, SelectAreaListFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        if (fragment != null && fragment.getVm() != null && fragment.getVm().getLevel() != AREA_PROVINCE) {
            fragment.getVm().downLevel();
        } else {
            super.onBackPressed();
            overridePendingTransition(0, R.anim.anim_right_out);
        }
    }

    private void setToolbarTitle(String title) {
        setTitle("");
        binding.tvToolbarTitle.setText(title);
    }
}

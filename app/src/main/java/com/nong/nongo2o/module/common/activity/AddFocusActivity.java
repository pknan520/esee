package com.nong.nongo2o.module.common.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityAddFocusBinding;
import com.nong.nongo2o.module.common.fragment.AddFocusListFragment;

/**
 * Created by Administrator on 2017-7-19.
 */

public class AddFocusActivity extends RxBaseActivity {

    private static final String TAG = "AddFocusActivity";

    private ActivityAddFocusBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddFocusActivity.class);
    }

//    @Override
//    protected ViewDataBinding getBinding() {
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_focus);
//        return binding;
//    }
//
//    @Override
//    public boolean canBack() {
//        return true;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_focus);
        initView();
    }

    private void initView() {
        replaceFragment(R.id.fl, AddFocusListFragment.newInstance(), AddFocusListFragment.TAG);
    }

//    public void setToolbarTitle(String title) {
//        setTitle("");
//        binding.tvToolbarTitle.setText(title);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

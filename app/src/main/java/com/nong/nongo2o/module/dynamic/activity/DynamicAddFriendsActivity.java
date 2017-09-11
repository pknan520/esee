package com.nong.nongo2o.module.dynamic.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityDynamicAddFriendsBinding;
import com.nong.nongo2o.module.dynamic.fragment.DynamicAddFriendsFragment;

/**
 * Created by Administrator on 2017-7-11.
 */

public class DynamicAddFriendsActivity extends RxBaseToolbarActivity {

    private ActivityDynamicAddFriendsBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, DynamicAddFriendsActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dynamic_add_friends);
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
        setTitle("");

        replaceFragment(R.id.fl, DynamicAddFriendsFragment.newInstance(), DynamicAddFriendsFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

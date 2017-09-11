package com.nong.nongo2o.module.personal.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityAddressMgrBinding;
import com.nong.nongo2o.module.personal.fragment.AddressListFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class AddressMgrActivity extends RxBaseToolbarActivity {

    public static int ADDR_MGR = 0, ADDR_SEL = 1;

    private ActivityAddressMgrBinding binding;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddressMgrActivity.class);
        intent.putExtra("status", ADDR_MGR);
        return intent;
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_mgr);
        return binding;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setToolbarTitle("管理地址");
        replaceFragment(R.id.fl, AddressListFragment.newInstance(getIntent().getIntExtra("status", ADDR_MGR)), AddressListFragment.TAG);
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

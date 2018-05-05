package com.nong.nongo2o.module.personal.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityWithdrawBinding;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.module.personal.fragment.WithdrawFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PANYJ7 on 2018-3-20.
 */

public class WithdrawActivity extends RxBaseToolbarActivity {

    private static final String TAG = "WithdrawActivity";

    private static String[] tabArray = {"申请提现", "提现历史"};
    public static String[] statuses = {"8", "9,10"};

    private ActivityWithdrawBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, WithdrawActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw);
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

        List<Fragment> fragmentList = new ArrayList<>();
        for (String status : statuses) {
            fragmentList.add(WithdrawFragment.newInstance(status));
        }

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);
        binding.tab.setupWithViewPager(binding.vp);

        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = binding.tab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.item_main_top_tab);
                if (tab.getCustomView() != null) {
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
                    tv.setText(tabArray[i]);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

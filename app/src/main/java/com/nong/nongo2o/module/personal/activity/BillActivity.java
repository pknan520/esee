package com.nong.nongo2o.module.personal.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityBillBinding;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.module.personal.fragment.BillListFragment;
import com.nong.nongo2o.module.personal.viewModel.ItemBillListVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-7-17.
 */

public class BillActivity extends RxBaseToolbarActivity {

    private static String[] tabArray = {"收入记录", "支出记录"};

    private ActivityBillBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, BillActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill);
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
        setTitle("");

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(BillListFragment.newInstance(ItemBillListVM.TYPE_INCOME));
        fragmentList.add(BillListFragment.newInstance(ItemBillListVM.TYPE_DISBURSEMENT));

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

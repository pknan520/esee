package com.nong.nongo2o.module.personal.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.ItemOrderCenterTabBinding;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.databinding.FragmentOrderListTotalBinding;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.viewModel.OrderCenterVM;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderListTotalFragment extends RxFragment {

    public static final String TAG = "OrderListTotalFragment";

    private static String[] tabArray = {"全部", "待付款", "待发货", "待收货", "待评价", "已完成", "已取消"};
    public static int[] statuses = {-99, 0, 1, 2, 3, 4, -1};

    private FragmentOrderListTotalBinding binding;
    private OrderCenterVM vm;
    private boolean isMerchantMode;

    public static OrderListTotalFragment newInstance(boolean isMerchantMode, int pos) {
        Bundle args = new Bundle();
        args.putBoolean("isMerchantMode", isMerchantMode);
        args.putInt("pos", pos);
        OrderListTotalFragment orderListTotalFragment = new OrderListTotalFragment();
        orderListTotalFragment.setArguments(args);

        return orderListTotalFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isMerchantMode = getArguments().getBoolean("isMerchantMode", false);

        if (vm == null) {
            vm = new OrderCenterVM(this, isMerchantMode, tabArray.length);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderListTotalBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        ((OrderCenterActivity) getActivity()).setToolbarTitle("我的订单");

        List<Fragment> fragmentList = new ArrayList<>();
        for (int i : statuses) {
            fragmentList.add(OrderListFragment.newInstance(i, isMerchantMode));
        }

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);
        binding.tab.setupWithViewPager(binding.vp);
        binding.tab.setTabMode(TabLayout.MODE_SCROLLABLE);

        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = binding.tab.getTabAt(i);
            if (tab != null) {
                ItemOrderCenterTabBinding tabBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.item_order_center_tab, null, false);
                tab.setCustomView(tabBinding.getRoot());
                tabBinding.setViewModel(vm.itemTabVMs.get(i));
                tabBinding.tv.setText(tabArray[i]);
            }
        }

        binding.vp.setCurrentItem(getArguments().getInt("pos", 0));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((OrderCenterActivity) getActivity()).setToolbarTitle("我的订单");
        }
    }
}

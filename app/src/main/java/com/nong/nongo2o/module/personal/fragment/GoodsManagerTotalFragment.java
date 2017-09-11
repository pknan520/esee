package com.nong.nongo2o.module.personal.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.databinding.FragmentGoodsManagerTotalBinding;
import com.nong.nongo2o.module.personal.activity.GoodsManagerActivity;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-6-27.
 */

public class GoodsManagerTotalFragment extends RxFragment {

    public static final String TAG = "GoodsManagerTotalFragme";

    private static String[] tabArray = {"上架", "售罄", "下架"};

    private FragmentGoodsManagerTotalBinding binding;

    public static GoodsManagerTotalFragment newInstance() {
        return new GoodsManagerTotalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGoodsManagerTotalBinding.inflate(inflater, container, false);
        initView(binding);
        return binding.getRoot();
    }

    private void initView(FragmentGoodsManagerTotalBinding binding) {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(GoodsManagerFragment.newInstance(1));
        fragmentList.add(GoodsManagerFragment.newInstance(2));
        fragmentList.add(GoodsManagerFragment.newInstance(3));

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);

        TabLayout tabLayout = ((GoodsManagerActivity) getActivity()).getTabLayout();
        tabLayout.setupWithViewPager(binding.vp);

        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((GoodsManagerActivity) getActivity()).getTabLayout();
        }
    }
}

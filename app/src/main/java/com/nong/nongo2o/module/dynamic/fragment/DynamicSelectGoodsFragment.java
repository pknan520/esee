package com.nong.nongo2o.module.dynamic.fragment;

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
import com.nong.nongo2o.databinding.FragmentDynamicSelectGoodsBinding;
import com.nong.nongo2o.module.dynamic.activity.DynamicPublishActivity;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-7-1.
 */

public class DynamicSelectGoodsFragment extends RxFragment {

    public static final String TAG = "DynamicSelectGoodsFragment";
    private static final String[] tabArray = {"我的商品", "我买到的"};
    private static final int MY_GOODS = 0, BOUGHT_GOODS = 1;

    private FragmentDynamicSelectGoodsBinding binding;

    public static DynamicSelectGoodsFragment newInstance() {
        return new DynamicSelectGoodsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDynamicSelectGoodsBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        ((DynamicPublishActivity) getActivity()).setToolbarTitle("");

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(DynamicGoodsListFragment.newInstance(MY_GOODS));
        fragmentList.add(DynamicGoodsListFragment.newInstance(BOUGHT_GOODS));

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);

        TabLayout tabLayout = ((DynamicPublishActivity) getActivity()).getTabLayout();
        tabLayout.setVisibility(View.VISIBLE);
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
}

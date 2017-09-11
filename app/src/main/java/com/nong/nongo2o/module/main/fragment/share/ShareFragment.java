package com.nong.nongo2o.module.main.fragment.share;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.databinding.FragmentShareBinding;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-6-22.
 */

public class ShareFragment extends RxFragment {

    private static String[] tabArray = {"最新", "附近", "我的"};

    private FragmentShareBinding binding;

    public static ShareFragment newInstance() {
        return new ShareFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShareBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(ShareListFragment.newInstance(0));
        fragmentList.add(ShareListFragment.newInstance(1));
        fragmentList.add(ShareMineFragment.newInstance());

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);
        binding.tab.setupWithViewPager(binding.vp);

        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = binding.tab.getTabAt(i);
            if (tab != null && TextUtils.isEmpty(tab.getText())) {
                tab.setText(tabArray[i]);
            }
        }
    }
}

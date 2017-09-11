package com.nong.nongo2o.module.personal.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.FragmentPersonalHomeBinding;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.module.personal.viewModel.PersonalHomeVM;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-6-30.
 */

public class PersonalHomeFragment extends RxFragment {

    public static final String TAG = "PersonalHomeFragment";

    private static String[] tabArray = {"TA的动态", "TA的宝贝"};

    private FragmentPersonalHomeBinding binding;
    private PersonalHomeVM vm;

    public static PersonalHomeFragment newInstance() {
        return new PersonalHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new PersonalHomeVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalHomeBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        getActivity().setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(PersonalDynamicFragment.newInstance());
        fragmentList.add(PersonalGoodsFragment.newInstance());

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);
        binding.tab.setupWithViewPager(binding.vp);

        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = binding.tab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.item_goods_tap);
                if (tab.getCustomView() != null) {
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
                    tv.setText(tabArray[i]);
                }
            }
        }
    }
}

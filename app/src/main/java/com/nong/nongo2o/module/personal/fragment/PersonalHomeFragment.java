package com.nong.nongo2o.module.personal.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.FragmentPersonalHomeBinding;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.bean.UserInfo;
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

    private FragmentPersonalHomeBinding binding;
    private PersonalHomeVM vm;
    private SimpleUser user;

    private LocalBroadcastManager lbm;

    public static PersonalHomeFragment newInstance(SimpleUser user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        PersonalHomeFragment fragment = new PersonalHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (SimpleUser) getArguments().getSerializable("user");
        if (vm == null) {
            vm = new PersonalHomeVM(this, user);
        }
        registerReceiver();
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

        binding.collapsingToolbar.setTitle(user.getUserNick());
        binding.collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        boolean isSeller = ((SimpleUser) getArguments().getSerializable("user")).getUserType() == 1;

        List<Fragment> fragmentList = new ArrayList<>();
        if (isSeller) {
            fragmentList.add(PersonalGoodsFragment.newInstance((SimpleUser) getArguments().getSerializable("user")));
            fragmentList.add(PersonalDynamicFragment.newInstance((SimpleUser) getArguments().getSerializable("user")));
            fragmentList.add(PersonalBuyFragment.newInstance((SimpleUser) getArguments().getSerializable("user")));
        } else {
            fragmentList.add(PersonalDynamicFragment.newInstance((SimpleUser) getArguments().getSerializable("user")));
            fragmentList.add(PersonalBuyFragment.newInstance((SimpleUser) getArguments().getSerializable("user")));
        }
//        fragmentList.add(PersonalDynamicFragment.newInstance((SimpleUser) getArguments().getSerializable("user")));
//        if (isSeller) {
//            fragmentList.add(PersonalGoodsFragment.newInstance((SimpleUser) getArguments().getSerializable("user")));
//        } else {
//            fragmentList.add(PersonalBuyFragment.newInstance((SimpleUser) getArguments().getSerializable("user")));
//        }

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);
        binding.tab.setupWithViewPager(binding.vp);

//        String[] tabArray = {"TA的动态", ((SimpleUser) getArguments().getSerializable("user")).getUserType() == 1 ? "TA的宝贝" : "购买宝贝"};
        String[] sellerTab = {"TA的宝贝", "TA的动态", "购买宝贝"};
        String[] cusTab = {"TA的动态", "购买宝贝"};

        for (int i = 0; i < (isSeller ? sellerTab.length : cusTab.length); i++) {
            TabLayout.Tab tab = binding.tab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.item_goods_tap);
                if (tab.getCustomView() != null) {
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
                    tv.setText(isSeller ? sellerTab[i] : cusTab[i]);
                }
            }
        }
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateDynamicNum");
        filter.addAction("updateGoodsNum");
        lbm.registerReceiver(updateReceiver, filter);
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "updateDynamicNum":
                    if (vm != null) vm.setDynamicNum(intent.getIntExtra("dynamicNum", 0));
                    break;
                case "updateGoodsNum":
                    if (vm != null) vm.setGoodsNum(intent.getIntExtra("goodsNum", 0));
                    break;
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        lbm.unregisterReceiver(updateReceiver);
    }
}

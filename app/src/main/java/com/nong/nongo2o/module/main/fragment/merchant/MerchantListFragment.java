package com.nong.nongo2o.module.main.fragment.merchant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.nong.nongo2o.databinding.FragmentMerchantListBinding;
import com.nong.nongo2o.module.main.viewModel.merchant.MerchantListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-23.
 */

public class MerchantListFragment extends RxFragment {

    private FragmentMerchantListBinding binding;
    private MerchantListVM vm;
    private int type;

    private LocalBroadcastManager lbm;

    public static MerchantListFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        MerchantListFragment fragment = new MerchantListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
        if (vm == null) {
            vm = new MerchantListVM(this, type);
        }
        registerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMerchantListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        binding.slider.setPresetTransformer(SliderLayout.Transformer.Default);

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.setNestedScrollingEnabled(false);
    }

    /**
     * 刷新广播接收器
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("loginSuccess");
        filter.addAction("refreshMerchantList");
        if (type == MerchantFragment.MERCHANT_FOCUS) filter.addAction("refreshFocus");
        lbm.registerReceiver(loginReceiver, filter);
    }

    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (vm != null) vm.initData();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(loginReceiver);
    }
}

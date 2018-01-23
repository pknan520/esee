package com.nong.nongo2o.module.main.fragment.dynamic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentDynamicListBinding;
import com.nong.nongo2o.module.main.viewModel.dynamic.DynamicListVM;
import com.nong.nongo2o.widget.recyclerView.StaggerItemDecoration;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-22.
 */

public class DynamicListFragment extends RxFragment {

    private FragmentDynamicListBinding binding;
    private DynamicListVM vm;
    private int status;

    private LocalBroadcastManager lbm;

    public static DynamicListFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt("status", status);
        DynamicListFragment fragment = new DynamicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getArguments().getInt("status");
        if (vm == null) {
            vm = new DynamicListVM(this, status);
        }
        registerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDynamicListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.rv.addItemDecoration(new StaggerItemDecoration(12));
//        binding.rv.addItemDecoration(new DividerItemDecoration(getActivity(), 24, 24, ContextCompat.getColor(getActivity(), android.R.color.transparent)));
    }

    /**
     * 刷新广播接收器
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("loginSuccess");
        filter.addAction("refreshDynamicList");
        if (status == DynamicFragment.DYNAMIC_FOCUS) filter.addAction("refreshFocus");
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

package com.nong.nongo2o.module.personal.fragment;

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

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentTradeBillBinding;
import com.nong.nongo2o.databinding.FragmentWithdrawBinding;
import com.nong.nongo2o.module.personal.viewModel.WithdrawVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by PANYJ7 on 2018-3-20.
 */

public class WithdrawFragment extends RxBaseFragment {

    private FragmentWithdrawBinding binding;
    private WithdrawVM vm;

    private LocalBroadcastManager lbm;

    public static WithdrawFragment newInstance(String status) {
        Bundle args = new Bundle();
        args.putString("status", status);
        WithdrawFragment fragment = new WithdrawFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new WithdrawVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWithdrawBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider_15px));

        registerReceiver();
    }

    /**
     * 广播接收器（更新）
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateWithdrawList");
        lbm.registerReceiver(updateReceiver, filter);
    }

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (vm != null) vm.initData();
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        lbm.unregisterReceiver(updateReceiver);
    }
}

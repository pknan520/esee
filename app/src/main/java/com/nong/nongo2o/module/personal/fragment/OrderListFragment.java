package com.nong.nongo2o.module.personal.fragment;

import android.app.AlertDialog;
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

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.DialogInputExBinding;
import com.nong.nongo2o.databinding.FragmentOrderListBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.personal.viewModel.DialogExVM;
import com.nong.nongo2o.module.personal.viewModel.OrderListVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderListFragment extends RxBaseFragment {

    private FragmentOrderListBinding binding;
    private OrderListVM vm;
    private String status;
    private boolean isMerchantMode;

    private LocalBroadcastManager lbm;

    private AlertDialog exDialog;
    private DialogInputExBinding exBinding;

    public static OrderListFragment newInstance(String status,boolean isMerchantMode) {
        Bundle args = new Bundle();
        args.putString("status", status);
        args.putBoolean("isMerchantMode",isMerchantMode);
        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getArguments().getString("status");
        isMerchantMode = getArguments().getBoolean("isMerchantMode",false);

        if (vm == null) {
            vm = new OrderListVM(this, status,isMerchantMode);
        }

        registerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        initExDialog();
    }

    /**
     * 广播接收器（更新）
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateOrderList");
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

    /**
     * 显示空数据页面
     */
    public void showEmptyView() {

    }

    /**
     * 发货信息输入框
     */
    private void initExDialog() {
        exBinding = DialogInputExBinding.inflate(getActivity().getLayoutInflater(), null, false);

        exDialog = new AlertDialog.Builder(getActivity())
                .setView(exBinding.getRoot())
                .create();
    }

    public void showExDialog(DialogExVM.DialogExListener listener) {
        if (exDialog != null && !exDialog.isShowing()) {
            exBinding.setViewModel(new DialogExVM(getActivity(), exDialog, listener));
            exDialog.show();
        }
    }

    public void hideExDialog() {
        if (exDialog != null && exDialog.isShowing()) {
            exDialog.dismiss();
        }
    }

    /**
     * 收货确认框
     */
    public void showReceiveDialog(OrderDetailFragment.ReceiveDialogListener listener) {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("确认收货后将不能取消，请确定是否确认收货？")
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("确定", (dialog, which) -> {
                    if (listener != null) {
                        listener.confirmReceive();
                    }
                    dialog.dismiss();
                })
                .show();
    }
}

package com.nong.nongo2o.module.personal.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import com.nong.nongo2o.databinding.DialogRefundBinding;
import com.nong.nongo2o.databinding.FragmentOrderDetailBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.common.ConfirmDialogListener;
import com.nong.nongo2o.module.personal.activity.BillActivity;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.viewModel.DialogExVM;
import com.nong.nongo2o.module.personal.viewModel.DialogRefundVM;
import com.nong.nongo2o.module.personal.viewModel.OrderDetailVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderDetailFragment extends RxBaseFragment {

    public static final String TAG = "OrderDetailFragment";

    private FragmentOrderDetailBinding binding;
    private OrderDetailVM vm;

    private LocalBroadcastManager lbm;

    private AlertDialog exDialog;
    private DialogInputExBinding exBinding;
    private AlertDialog refundDialog;
    private DialogRefundBinding refundBinding;

    public static OrderDetailFragment newInstance(Order order, boolean isMerchantMode) {
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        args.putBoolean("isMerchantMode", isMerchantMode);
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new OrderDetailVM(this, (Order) getArguments().getSerializable("order"), getArguments().getBoolean("isMerchantMode"));
        }
        registerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        if (getActivity() instanceof OrderCenterActivity) {
            ((OrderCenterActivity) getActivity()).setToolbarTitle("订单详情");
        }
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.rvTrans.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.sv.smoothScrollTo(0, 0);

        initExDialog();
        initRefundDialog();
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
     * 退款申请输入框
     */
    private void initRefundDialog() {
        refundBinding = DialogRefundBinding.inflate(getActivity().getLayoutInflater(), null, false);

        refundDialog = new AlertDialog.Builder(getActivity())
                .setView(refundBinding.getRoot())
                .create();
    }

    public void showRefundDialog(Order order, boolean isSaler, boolean isAgree, DialogRefundVM.DialogRefundListener listener) {
        if (refundDialog != null && ! refundDialog.isShowing()) {
            refundBinding.setViewModel(new DialogRefundVM(getActivity(), refundDialog, listener, order, isSaler, isAgree));
            refundDialog.show();
        }
    }

    public void hideRefundDialog() {
        if (refundDialog != null && refundDialog.isShowing())
            refundDialog.dismiss();
    }

    /**
     * 操作确认框
     */
    public void showConfirmDialog(String content, ConfirmDialogListener listener) {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage(content)
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("确定", (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirm();
                    }
                    dialog.dismiss();
                })
                .show();
    }

    /**
     * 广播接收器
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("paySuccess");
        filter.addAction("payFail");
        lbm.registerReceiver(payReceiver, filter);
    }

    private BroadcastReceiver payReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("paySuccess")) {
                Order order = (Order) getArguments().getSerializable("order");
                if (order != null) {
                    order.setOrderStatus(1);
//                    vm = new OrderDetailVM(OrderDetailFragment.this, order, getArguments().getBoolean("isMerchantMode"));
                    vm.iniData();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(payReceiver);
    }
}

package com.nong.nongo2o.module.personal.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.DialogInputExBinding;
import com.nong.nongo2o.databinding.FragmentOrderDetailBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.viewModel.DialogExVM;
import com.nong.nongo2o.module.personal.viewModel.OrderDetailVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderDetailFragment extends RxBaseFragment {

    public static final String TAG = "OrderDetailFragment";

    private FragmentOrderDetailBinding binding;
    private OrderDetailVM vm;

    private AlertDialog exDialog;
    private DialogInputExBinding exBinding;

    public interface ReceiveDialogListener {
        void confirmReceive();
    }

    // TODO: 2017-9-22 临时容错，以后删除
    public static OrderDetailFragment newInstance() {
        return new OrderDetailFragment();
    }

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
        ((OrderCenterActivity) getActivity()).setToolbarTitle("订单详情");
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.rvTrans.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.sv.smoothScrollTo(0, 0);

        initExDialog();
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

    /**
     * 收货确认框
     */
    public void showReceiveDialog(ReceiveDialogListener listener) {
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

package com.nong.nongo2o.module.common.buy.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentPayResultBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.common.CancelDialogListener;
import com.nong.nongo2o.module.common.ConfirmDialogListener;
import com.nong.nongo2o.module.common.buy.viewModel.PayResultVM;

/**
 * Created by Administrator on 2017-9-19.
 */

public class PayResultFragment extends RxBaseFragment {

    public static final String TAG = "PayResultFragment";

    private FragmentPayResultBinding binding;
    private PayResultVM vm;

    public static PayResultFragment newInstance(Order order, boolean payResult) {
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        args.putBoolean("payResult", payResult);
        PayResultFragment fragment = new PayResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new PayResultVM(this, getArguments().getBoolean("payResult", false));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPayResultBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        if (getArguments().getBoolean("payResult")) {
            showConfirmDialog("是否同步发表动态", () -> {
                if (vm != null && getArguments().getSerializable("order") != null)
                    vm.createMoment(((Order) getArguments().getSerializable("order")).getOrderDetails().get(0).getGoods());
            });
        }
    }

    /**
     * 操作确认框
     */
    public void showConfirmDialog(String content, ConfirmDialogListener confirm) {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage(content)
                .setCancelable(false)
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    if (confirm != null) confirm.onConfirm();
                })
                .show();
    }
}

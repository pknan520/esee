package com.nong.nongo2o.module.common.buy.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentPayBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.common.buy.activity.BuyActivity;
import com.nong.nongo2o.module.common.buy.viewModel.PayVM;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;

/**
 * Created by Administrator on 2017-9-19.
 */

public class PayFragment extends RxBaseFragment {

    public static final String TAG = "PayFragment";

    private FragmentPayBinding binding;
    private PayVM vm;

    private LocalBroadcastManager lbm;

    public static PayFragment newInstance(Order order) {
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        PayFragment fragment = new PayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new PayVM(this, (Order) getArguments().getSerializable("order"));
        }
        registerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPayBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((BuyActivity) getActivity()).setToolbarTitle("支付订单");

    }

    /**
     * 支付广播接收器
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("paySuccess");
        filter.addAction("payCancel");
        filter.addAction("payFail");
        lbm.registerReceiver(payReceiver, filter);
    }

    private BroadcastReceiver payReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean result = false;
            switch (intent.getAction()) {
                case "paySuccess":
                    result = true;
                    break;
                case "payFail":
                    cancelPay();
                    result = false;
                    break;
                case "payCancel":
                    cancelPay();
                    break;
            }
            ((RxBaseActivity) getActivity()).replaceFragment(R.id.fl, PayResultFragment.newInstance(result), PayResultFragment.TAG);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(payReceiver);
    }

    private void cancelPay() {
        if (vm != null) vm.cancelPay();
    }
}

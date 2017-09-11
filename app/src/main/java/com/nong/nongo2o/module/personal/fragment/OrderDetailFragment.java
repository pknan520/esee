package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentOrderDetailBinding;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.viewModel.OrderDetailVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderDetailFragment extends RxBaseFragment {

    public static final String TAG = "OrderDetailFragment";

    private FragmentOrderDetailBinding binding;
    private OrderDetailVM vm;

    public static OrderDetailFragment newInstance() {
        return new OrderDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new OrderDetailVM(this);
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
        binding.rvTrans.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.sv.smoothScrollTo(0, 0);
    }
}

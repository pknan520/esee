package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentOrderListBinding;
import com.nong.nongo2o.module.personal.viewModel.OrderListVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderListFragment extends RxBaseFragment {

    private FragmentOrderListBinding binding;
    private OrderListVM vm;
    private int status;
    private boolean isMerchantMode;

    public static OrderListFragment newInstance(int status,boolean isMerchantMode) {
        Bundle args = new Bundle();
        args.putInt("status", status);
        args.putBoolean("isMerchantMode",isMerchantMode);
        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getArguments().getInt("status");
        isMerchantMode = getArguments().getBoolean("isMerchantMode",false);

        if (vm == null) {
            vm = new OrderListVM(this, status,isMerchantMode);
        }
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
    }
}

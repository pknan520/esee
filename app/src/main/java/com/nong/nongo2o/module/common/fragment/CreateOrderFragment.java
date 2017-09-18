package com.nong.nongo2o.module.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentCreateOrderBinding;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.module.common.activity.BuyActivity;
import com.nong.nongo2o.module.common.viewModel.CreateOrderVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-7-17.
 */

public class CreateOrderFragment extends RxBaseFragment {

    public static final String TAG = "CreateOrderFragment";

    private FragmentCreateOrderBinding binding;
    private CreateOrderVM vm;
    private OrderDetail orderDetail;

    public static CreateOrderFragment newInstance(OrderDetail orderDetail) {
        CreateOrderFragment createOrderFragment = new CreateOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderDetail",orderDetail);
        createOrderFragment.setArguments(bundle);
        return createOrderFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetail = (OrderDetail) getArguments().getSerializable("orderDetail");
        if (vm == null) {
            vm = new CreateOrderVM(this ,orderDetail);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateOrderBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((BuyActivity) getActivity()).setToolbarTitle("确认订单");

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) ((BuyActivity) getActivity()).setToolbarTitle("确认订单");
    }
}

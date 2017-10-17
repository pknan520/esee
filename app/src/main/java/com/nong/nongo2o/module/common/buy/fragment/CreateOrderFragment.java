package com.nong.nongo2o.module.common.buy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentCreateOrderBinding;
import com.nong.nongo2o.entity.domain.Address;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.module.common.buy.activity.BuyActivity;
import com.nong.nongo2o.module.common.buy.viewModel.CreateOrderVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-7-17.
 */

public class CreateOrderFragment extends RxBaseFragment {

    public static final String TAG = "CreateOrderFragment";

    private FragmentCreateOrderBinding binding;
    private CreateOrderVM vm;

    // TODO: 2017-9-18 临时容错，以后删除
    public static CreateOrderFragment newInstance(OrderDetail orderDetail) {
        CreateOrderFragment createOrderFragment = new CreateOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderDetail",orderDetail);
        createOrderFragment.setArguments(bundle);
        return createOrderFragment;
    }

    public static CreateOrderFragment newInstance(ArrayList<OrderDetail> orderDetails) {
        Bundle args = new Bundle();
        args.putSerializable("orderDetails", orderDetails);
        CreateOrderFragment fragment = new CreateOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new CreateOrderVM(this , (ArrayList<OrderDetail>) getArguments().getSerializable("orderDetails"));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == -1 && vm != null) vm.setAddrInfo((Address) data.getSerializableExtra("address"));
                break;
        }
    }
}

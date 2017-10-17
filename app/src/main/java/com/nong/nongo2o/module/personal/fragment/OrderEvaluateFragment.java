package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentOrderEvaluateBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.viewModel.OrderEvaluateVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-9-23.
 */

public class OrderEvaluateFragment extends RxBaseFragment {

    public static final String TAG = "OrderEvaluateFragment";

    private FragmentOrderEvaluateBinding binding;
    private OrderEvaluateVM vm;

    public static OrderEvaluateFragment newInstance(Order order) {
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        OrderEvaluateFragment fragment = new OrderEvaluateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new OrderEvaluateVM(this, (Order) getArguments().getSerializable("order"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderEvaluateBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((OrderCenterActivity) getActivity()).setToolbarTitle("评价");

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 10, R.color.colorWindowBg));

//        ViewGroup.LayoutParams rlParams = binding.rlText.getLayoutParams();
//        ViewGroup.LayoutParams ratingBarParams = binding.ratingBar.getLayoutParams();
//        rlParams.width = ratingBarParams.width;
//        binding.rlText.setLayoutParams(rlParams);
    }
}

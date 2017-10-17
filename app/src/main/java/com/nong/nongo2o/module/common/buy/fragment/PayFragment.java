package com.nong.nongo2o.module.common.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentPayBinding;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.common.buy.viewModel.PayVM;

/**
 * Created by Administrator on 2017-9-19.
 */

public class PayFragment extends RxBaseFragment {

    public static final String TAG = "PayFragment";

    private FragmentPayBinding binding;
    private PayVM vm;

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

    }
}

package com.nong.nongo2o.module.merchant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentMerchantGoodsEvaluateBinding;
import com.nong.nongo2o.module.merchant.viewModel.MerchantGoodsEvaluateVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-7-12.
 */

public class MerchantGoodsEvaluateFragment extends RxFragment {

    public static final String TAG = "MerchantGoodsEvaluateFragment";

    private FragmentMerchantGoodsEvaluateBinding binding;
    private MerchantGoodsEvaluateVM vm;

    public static MerchantGoodsEvaluateFragment newInstance() {
        return new MerchantGoodsEvaluateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new MerchantGoodsEvaluateVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMerchantGoodsEvaluateBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.rv.setNestedScrollingEnabled(false);
    }
}

package com.nong.nongo2o.module.merchant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentMerchantSaleRecordBinding;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.module.merchant.viewModel.MerchantSaleRecordVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by PANYJ7 on 2018-3-6.
 */

public class MerchantSaleRecordFragment extends RxFragment {

    private static final String TAG = "MerchantSaleRecordFragment";

    private FragmentMerchantSaleRecordBinding binding;
    private MerchantSaleRecordVM vm;

    public static MerchantSaleRecordFragment newInstance(Goods good) {
        Bundle args = new Bundle();
        args.putSerializable("good", good);
        MerchantSaleRecordFragment fragment = new MerchantSaleRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new MerchantSaleRecordVM(this, (Goods) getArguments().getSerializable("good"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMerchantSaleRecordBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.rv.setNestedScrollingEnabled(false);
    }
}

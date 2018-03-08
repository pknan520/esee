package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentTradeBillBinding;
import com.nong.nongo2o.module.personal.viewModel.TradeBillVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by PANYJ7 on 2018-3-7.
 */

public class TradeBillFragment extends RxBaseFragment {

    private FragmentTradeBillBinding binding;
    private TradeBillVM vm;

    public static TradeBillFragment newInstance() {
        Bundle args = new Bundle();
        TradeBillFragment fragment = new TradeBillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new TradeBillVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTradeBillBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider_15px));
    }
}

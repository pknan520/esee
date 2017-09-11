package com.nong.nongo2o.module.dynamic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.FragmentDynamicGoodsListBinding;
import com.nong.nongo2o.module.dynamic.viewModel.DynamicGoodsListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-7-7.
 */

public class DynamicGoodsListFragment extends RxFragment {

    private FragmentDynamicGoodsListBinding binding;
    private DynamicGoodsListVM vm;

    public static DynamicGoodsListFragment newInstance() {
        return new DynamicGoodsListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new DynamicGoodsListVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDynamicGoodsListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, R.drawable.shape_vertical_divider));
    }
}

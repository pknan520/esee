package com.nong.nongo2o.module.main.fragment.dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentDynamicListBinding;
import com.nong.nongo2o.module.main.viewModel.dynamic.DynamicListVM;
import com.nong.nongo2o.widget.recyclerView.StaggerItemDecoration;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-22.
 */

public class DynamicListFragment extends RxFragment {

    private FragmentDynamicListBinding binding;
    private DynamicListVM vm;

    public static DynamicListFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt("status", status);
        DynamicListFragment fragment = new DynamicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int status = getArguments().getInt("status");
        if (vm == null) {
            vm = new DynamicListVM(this, status);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDynamicListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.rv.addItemDecoration(new StaggerItemDecoration(24));
    }

    public void showEmptyView() {
        binding.rv.setVisibility(View.GONE);
        binding.llEmpty.setVisibility(View.VISIBLE);
    }

    public void showContentView() {
        binding.rv.setVisibility(View.VISIBLE);
        binding.llEmpty.setVisibility(View.GONE);
    }

}

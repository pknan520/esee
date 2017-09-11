package com.nong.nongo2o.module.main.fragment.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentShareListBinding;
import com.nong.nongo2o.module.main.viewModel.share.ShareListVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-23.
 */

public class ShareListFragment extends RxFragment{

    private FragmentShareListBinding binding;
    private ShareListVM vm;

    public static ShareListFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt("status", status);
        ShareListFragment fragment = new ShareListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int status = getArguments().getInt("status");
        if (vm == null) {
            vm = new ShareListVM(this, status);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShareListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

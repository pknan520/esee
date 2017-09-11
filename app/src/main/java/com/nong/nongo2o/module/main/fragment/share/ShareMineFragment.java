package com.nong.nongo2o.module.main.fragment.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentShareMineBinding;
import com.nong.nongo2o.module.main.viewModel.share.ShareMineVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-23.
 */

public class ShareMineFragment extends RxFragment {

    private FragmentShareMineBinding binding;
    private ShareMineVM vm;

    public static ShareMineFragment newInstance() {
        return new ShareMineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new ShareMineVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShareMineBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

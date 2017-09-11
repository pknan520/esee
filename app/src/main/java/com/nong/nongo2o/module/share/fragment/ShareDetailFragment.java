package com.nong.nongo2o.module.share.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentShareDetailBinding;
import com.nong.nongo2o.module.share.viewModel.ShareDetailVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-7-3.
 */

public class ShareDetailFragment extends RxFragment {

    public static final String TAG = "ShareDetailFragment";

    private FragmentShareDetailBinding binding;
    private ShareDetailVM vm;

    public static ShareDetailFragment newInstance() {
        return new ShareDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new ShareDetailVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShareDetailBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rvDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

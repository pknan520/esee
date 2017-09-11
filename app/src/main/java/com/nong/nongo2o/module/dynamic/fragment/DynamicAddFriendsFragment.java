package com.nong.nongo2o.module.dynamic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentDynamicAddFragmentBinding;
import com.nong.nongo2o.module.dynamic.viewModel.DynamicAddFriendsVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-7-11.
 */

public class DynamicAddFriendsFragment extends RxFragment {

    public static final String TAG = "DynamicAddFriendsFragment";

    private FragmentDynamicAddFragmentBinding binding;
    private DynamicAddFriendsVM vm;

    public static DynamicAddFriendsFragment newInstance() {
        return new DynamicAddFriendsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new DynamicAddFriendsVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDynamicAddFragmentBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {

    }
}

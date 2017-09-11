package com.nong.nongo2o.module.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentAddFocusListBinding;
import com.nong.nongo2o.module.common.activity.AddFocusActivity;
import com.nong.nongo2o.module.common.viewModel.AddFocusListVM;

/**
 * Created by Administrator on 2017-7-19.
 */

public class AddFocusListFragment extends RxBaseFragment {

    public static final String TAG = "AddFocusListFragment";

    private FragmentAddFocusListBinding binding;
    private AddFocusListVM vm;

    public static AddFocusListFragment newInstance() {
        return new AddFocusListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new AddFocusListVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddFocusListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((AddFocusActivity) getActivity()).setToolbarTitle("关注好友");

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

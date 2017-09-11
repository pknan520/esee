package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.databinding.FragmentAddressListBinding;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.viewModel.AddressListVM;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-29.
 */

public class AddressListFragment extends RxFragment {

    public static final String TAG = "AddressListFragment";

    private FragmentAddressListBinding binding;
    private AddressListVM vm;

    public static AddressListFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt("status", status);
        AddressListFragment fragment = new AddressListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new AddressListVM(this, getArguments().getInt("status"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddressListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((AddressMgrActivity) getActivity()).setToolbarTitle("管理地址");
            vm.initData();
        }
    }

}

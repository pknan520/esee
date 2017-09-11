package com.nong.nongo2o.module.message.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentSysMsgListBinding;
import com.nong.nongo2o.module.message.activity.SysMsgActivity;
import com.nong.nongo2o.module.message.viewModel.SysMsgListVM;

/**
 * Created by Administrator on 2017-7-19.
 */

public class SysMsgListFragment extends RxBaseFragment {

    public static final String TAG = "SysMsgListFragment";

    private FragmentSysMsgListBinding binding;
    private SysMsgListVM vm;

    public static SysMsgListFragment newInstance() {
        return new SysMsgListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new SysMsgListVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSysMsgListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((SysMsgActivity) getActivity()).setToolbarTitle("系统消息");

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

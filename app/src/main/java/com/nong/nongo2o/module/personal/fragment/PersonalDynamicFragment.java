package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentPersonalDynamicBinding;
import com.nong.nongo2o.module.personal.viewModel.PersonalDynamicVM;

/**
 * Created by Administrator on 2017-7-15.
 */

public class PersonalDynamicFragment extends RxBaseFragment {

    private FragmentPersonalDynamicBinding binding;
    private PersonalDynamicVM vm;

    public static PersonalDynamicFragment newInstance() {
        return new PersonalDynamicFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new PersonalDynamicVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalDynamicBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }
}

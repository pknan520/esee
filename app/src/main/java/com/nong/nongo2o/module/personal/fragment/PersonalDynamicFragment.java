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
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.module.personal.viewModel.PersonalDynamicVM;
import com.nong.nongo2o.widget.recyclerView.StaggerItemDecoration;

/**
 * Created by Administrator on 2017-7-15.
 */

public class PersonalDynamicFragment extends RxBaseFragment {

    private FragmentPersonalDynamicBinding binding;
    private PersonalDynamicVM vm;

    // TODO: 2017-9-15 临时容错，以后删除
    public static PersonalDynamicFragment newInstance() {
        return new PersonalDynamicFragment();
    }

    public static PersonalDynamicFragment newInstance(SimpleUser user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        PersonalDynamicFragment fragment = new PersonalDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new PersonalDynamicVM(this, (SimpleUser) getArguments().getSerializable("user"));
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
        binding.rv.addItemDecoration(new StaggerItemDecoration(24));
    }
}

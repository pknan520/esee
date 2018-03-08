package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentPersonalBuyBinding;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.module.personal.viewModel.PersonalBuyVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by PANYJ7 on 2018-3-5.
 */

public class PersonalBuyFragment extends RxBaseFragment {

    private FragmentPersonalBuyBinding binding;
    private PersonalBuyVM vm;

    public static PersonalBuyFragment newInstance(SimpleUser user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        PersonalBuyFragment fragment = new PersonalBuyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new PersonalBuyVM(this, (SimpleUser) getArguments().getSerializable("user"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalBuyBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }
}

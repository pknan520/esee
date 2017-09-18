package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentPersonalGoodsBinding;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.module.personal.viewModel.PersonalGoodsVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-7-15.
 */

public class PersonalGoodsFragment extends RxBaseFragment {

    private FragmentPersonalGoodsBinding binding;
    private PersonalGoodsVM vm;

    // TODO: 2017-9-15 临时容错，以后删除
    public static PersonalGoodsFragment newInstance() {
        return new PersonalGoodsFragment();
    }

    public static PersonalGoodsFragment newInstance(SimpleUser user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        PersonalGoodsFragment fragment = new PersonalGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new PersonalGoodsVM(this, (SimpleUser) getArguments().getSerializable("user"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalGoodsBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }
}

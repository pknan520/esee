package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentMyInvitersBinding;
import com.nong.nongo2o.module.personal.activity.InviteActivity;
import com.nong.nongo2o.module.personal.viewModel.MyInvitersVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * Created by PANYJ7 on 2018-1-10.
 */

public class MyInvitersFragment extends RxBaseFragment {

    public static final String TAG = "MyInvitersFragment";

    private FragmentMyInvitersBinding binding;
    private MyInvitersVM vm;

    public static MyInvitersFragment newInstance() {
        return new MyInvitersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) vm = new MyInvitersVM(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyInvitersBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((InviteActivity) getActivity()).setToolbarTitle("我邀请的人");

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }

}

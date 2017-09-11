package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentFansListBinding;
import com.nong.nongo2o.module.personal.viewModel.FansListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-7-17.
 */

public class FansListFragment extends RxBaseFragment {

    public static final String TAG = "FansListFragment";

    private FragmentFansListBinding binding;
    private FansListVM vm;

    public static FansListFragment newInstance() {
        return new FansListFragment();
    }

    public static FansListFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt("status", status);
        FansListFragment fragment = new FansListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new FansListVM(this, getArguments().getInt("status"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFansListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }
}

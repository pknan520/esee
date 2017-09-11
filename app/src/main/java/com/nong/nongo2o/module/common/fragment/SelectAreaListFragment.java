package com.nong.nongo2o.module.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentSelectAreaListBinding;
import com.nong.nongo2o.entities.common.Area;
import com.nong.nongo2o.module.common.viewModel.SelectAreaListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-8-19.
 */

public class SelectAreaListFragment extends RxBaseFragment {

    public static final String TAG = "SelectAreaListFragment";

    private SelectAreaListVM vm;
    private FragmentSelectAreaListBinding binding;

    public static SelectAreaListFragment newInstance(int level, @Nullable Area areaP, @Nullable Area areaC) {
        Bundle args = new Bundle();
        args.putInt("level", level);
        args.putParcelable("areaP", areaP);
        args.putParcelable("areaC", areaC);
        SelectAreaListFragment fragment = new SelectAreaListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new SelectAreaListVM(this, getArguments().getInt("level"), getArguments().getParcelable("areaP"), getArguments().getParcelable("areaC"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectAreaListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }
}

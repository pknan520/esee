package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentBillListBinding;
import com.nong.nongo2o.module.personal.viewModel.BillListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-7-17.
 */

public class BillListFragment extends RxBaseFragment {

    private FragmentBillListBinding binding;
    private BillListVM vm;

    public static BillListFragment newInstance(int billType) {
        Bundle args = new Bundle();
        args.putInt("billType", billType);
        BillListFragment fragment = new BillListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int billType = getArguments().getInt("billType");
        if (vm == null) {
            vm = new BillListVM(this, billType);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBillListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider_15px));
    }
}

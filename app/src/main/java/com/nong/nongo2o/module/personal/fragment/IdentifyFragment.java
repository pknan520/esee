package com.nong.nongo2o.module.personal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentIdentifyBinding;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;
import com.nong.nongo2o.module.personal.viewModel.IdentifyVM;

/**
 * Created by PANYJ7 on 2018-5-2.
 */

public class IdentifyFragment extends RxBaseFragment {

    public static final String TAG = "IdentifyFragment";

    private FragmentIdentifyBinding binding;
    private IdentifyVM vm;

    public static IdentifyFragment newInstance() {
        return new IdentifyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) vm = new IdentifyVM(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIdentifyBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((IdentifyActivity)getActivity()).setToolbarTitle("实名认证");
    }
}

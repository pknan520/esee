package com.nong.nongo2o.module.personal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentSettingBinding;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.module.personal.activity.SettingActivity;
import com.nong.nongo2o.module.personal.viewModel.SettingVM;

/**
 * Created by Administrator on 2017-7-15.
 */

public class SettingFragment extends RxBaseFragment {

    public static final String TAG = "SettingFragment";
    public static final int GET_AREA = 100;

    private FragmentSettingBinding binding;
    private SettingVM vm;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new SettingVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((SettingActivity) getActivity()).setToolbarTitle("个人资料");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((SettingActivity) getActivity()).setToolbarTitle("个人资料");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_AREA:
                if (resultCode == -1 && vm != null) {
                    vm.setCities((City) data.getSerializableExtra("cityP"), (City) data.getSerializableExtra("cityC"), (City) data.getSerializableExtra("cityD"));
                }
                break;
        }
    }
}

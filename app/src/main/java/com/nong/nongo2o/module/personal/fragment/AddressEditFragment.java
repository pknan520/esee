package com.nong.nongo2o.module.personal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentAddressEditBinding;
import com.nong.nongo2o.databinding.PopupAreaBinding;
import com.nong.nongo2o.entities.common.Area;
import com.nong.nongo2o.entity.domain.Address;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.module.common.popup.AreaPopup;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.viewModel.AddressEditVM;

/**
 * Created by Administrator on 2017-6-30.
 */

public class AddressEditFragment extends RxBaseFragment {

    public static final String TAG = "AddressEditFragment";
    public static final int GET_AREA = 100;

    private FragmentAddressEditBinding binding;
    private AddressEditVM vm;

    public static AddressEditFragment newInstance() {
        return new AddressEditFragment();
    }

    public static AddressEditFragment newInstance(Address address) {
        Bundle args = new Bundle();
        args.putSerializable("address", address);
        AddressEditFragment fragment = new AddressEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new AddressEditVM(this, getArguments() != null ? (Address) getArguments().getSerializable("address") : null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddressEditBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((AddressMgrActivity) getActivity()).setToolbarTitle("编辑收货地址");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((AddressMgrActivity) getActivity()).setToolbarTitle("编辑收货地址");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_AREA:
                if (resultCode == -1) {
                    vm.setCities((City) data.getSerializableExtra("cityP"), (City) data.getSerializableExtra("cityC"),
                            (City) data.getSerializableExtra("cityD"), (City) data.getSerializableExtra("cityS"));
                }
                break;
        }
    }
}

package com.nong.nongo2o.module.personal.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentAgreementBinding;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;

/**
 * Created by PANYJ7 on 2018-5-2.
 */

public class AgreementFragment extends RxBaseFragment {

    public static final String TAG = "AgreementFragment";

    private FragmentAgreementBinding binding;

    public static AgreementFragment newInstance() {
        return new AgreementFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgreementBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        ((IdentifyActivity)getActivity()).setToolbarTitle("用户协议");
    }
}

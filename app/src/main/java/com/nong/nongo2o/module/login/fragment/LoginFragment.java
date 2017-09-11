package com.nong.nongo2o.module.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentLoginBinding;
import com.nong.nongo2o.module.login.viewModel.LoginVM;

/**
 * Created by Administrator on 2017-8-4.
 */

public class LoginFragment extends RxBaseFragment {

    public static final String TAG = "LoginFragment";

    private FragmentLoginBinding binding;
    private LoginVM vm;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new LoginVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {

    }
}

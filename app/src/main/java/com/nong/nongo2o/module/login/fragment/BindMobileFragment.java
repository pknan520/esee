package com.nong.nongo2o.module.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentBindMobileBinding;
import com.nong.nongo2o.entity.response.WxAccessToken;
import com.nong.nongo2o.entity.response.WxInfo;
import com.nong.nongo2o.module.login.viewModel.BindMobileVM;

/**
 * Created by Administrator on 2017-9-23.
 */

public class BindMobileFragment extends RxBaseFragment {

    public static final String TAG = "BindMobileFragment";

    private FragmentBindMobileBinding binding;
    private BindMobileVM vm;

    public static BindMobileFragment newInstance(WxAccessToken wxAccessToken, WxInfo wxInfo) {
        Bundle args = new Bundle();
        args.putSerializable("wxAccessToken", wxAccessToken);
        args.putSerializable("wxInfo", wxInfo);
        BindMobileFragment fragment = new BindMobileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new BindMobileVM(this, (WxAccessToken) getArguments().getSerializable("wxAccessToken"), (WxInfo) getArguments().getSerializable("wxInfo"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBindMobileBinding.inflate(inflater, container, false);
        initView(savedInstanceState);
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView(Bundle savedInstanceState) {
        binding.btnTime.onCreate(savedInstanceState);
        binding.btnTime.setTextAfter("s      ").setTextBefore("获取验证码").setLength(60 * 1000);
    }

    @Override
    public void onDestroy() {
        binding.btnTime.onDestroy();
        super.onDestroy();
    }
}

package com.nong.nongo2o.module.login.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentLoginBinding;
import com.nong.nongo2o.entity.response.WxAccessToken;
import com.nong.nongo2o.entity.response.WxInfo;
import com.nong.nongo2o.module.login.viewModel.LoginVM;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.service.InitDataService;

/**
 * Created by Administrator on 2017-8-4.
 */

public class LoginFragment extends RxBaseFragment {

    public static final String TAG = "LoginFragment";

    private FragmentLoginBinding binding;
    private LoginVM vm;

    private LocalBroadcastManager lbm;

    public static LoginFragment newInstance(boolean canClose) {
        Bundle args = new Bundle();
        args.putBoolean("canClose", canClose);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new LoginVM(this, getArguments().getBoolean("canClose"));
        }
        registerReceiver();
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

    /**
     * 广播接收器（更新）
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("loginSuccess");
        filter.addAction("loginFail");
        lbm.registerReceiver(updateReceiver, filter);
    }

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((RxBaseActivity) getActivity()).dismissLoading();
            switch (intent.getAction()) {
                case "loginSuccess":
                    toMainActivity();
                    break;
                case "loginFail":
                    toBindMobile((WxAccessToken) intent.getSerializableExtra("wxAccessToken"), (WxInfo) intent.getSerializableExtra("wxInfo"));
                    break;
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        lbm.unregisterReceiver(updateReceiver);
    }

    private void toMainActivity() {
        getActivity().startService(new Intent(getActivity(), InitDataService.class));
        getActivity().startActivity(MainActivity.newIntent(getActivity()));
        getActivity().finish();
    }

    private void toBindMobile(WxAccessToken wxAccessToken, WxInfo wxInfo) {
        ((RxBaseActivity) getActivity()).switchFragment(R.id.fl, this, BindMobileFragment.newInstance(wxAccessToken, wxInfo), BindMobileFragment.TAG);
    }

}

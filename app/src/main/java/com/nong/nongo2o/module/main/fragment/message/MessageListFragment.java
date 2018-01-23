package com.nong.nongo2o.module.main.fragment.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentMessageListBinding;
import com.nong.nongo2o.module.main.viewModel.message.MessageListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-7-19.
 */

public class MessageListFragment extends RxBaseFragment {

    private FragmentMessageListBinding binding;
    private MessageListVM vm;
    private LocalBroadcastManager lbm;

    private boolean hidden;

    public static MessageListFragment newInstance() {
        return new MessageListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new MessageListVM(this);
        }
        registerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        getActivity().setTitle("");
        binding.tvToolbarTitle.setText("消息");

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden && vm != null) {
            vm.refresh();
            vm.checkUnreadMessage();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && vm != null) {
            vm.refresh();
            vm.checkUnreadMessage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vm != null) EMClient.getInstance().chatManager().removeMessageListener(vm.getEmListener());
        lbm.unregisterReceiver(loginReceiver);
    }

    /**
     * 刷新广播接收器
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("loginSuccess");
        lbm.registerReceiver(loginReceiver, filter);
    }

    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (vm != null) vm.initData();
        }
    };
}

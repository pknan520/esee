package com.nong.nongo2o.module.personal.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.FragmentGoodsManagerBinding;
import com.nong.nongo2o.databinding.PopupMenuBinding;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.module.personal.viewModel.GoodsManagerVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-26.
 */

public class GoodsManagerFragment extends RxFragment {

    private static final String TAG = "GoodsManagerFragment";

    private FragmentGoodsManagerBinding binding;
    private GoodsManagerVM vm;
    private PopupWindow popupMenu;
    private PopupMenuBinding popupBinding;

    private LocalBroadcastManager lbm;

    public static GoodsManagerFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt("status", status);
        GoodsManagerFragment fragment = new GoodsManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int status = getArguments().getInt("status");
        if (vm == null) {
            vm = new GoodsManagerVM(this, status);
        }
        registerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGoodsManagerBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));

        popupBinding = PopupMenuBinding.inflate(getActivity().getLayoutInflater(), null, false);
        popupBinding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        popupMenu = new PopupWindow(popupBinding.getRoot(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupMenu.setFocusable(true);
        popupMenu.setAnimationStyle(R.style.PopupAnimBottom);
        popupMenu.setBackgroundDrawable(new ColorDrawable(0x80000000));
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popupBinding.getRoot().setOnTouchListener((v, event) -> {
            int height = popupBinding.llContainer.getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    popupMenu.dismiss();
                }
            }
            return true;
        });
    }

    public void showPopupMenu(Goods goods) {
        if (popupMenu != null && !popupMenu.isShowing()) {

            popupBinding.setViewModel(vm.new PopupVM(popupMenu, goods));
            popupMenu.update();
            popupMenu.showAtLocation(binding.llContainer, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("goodsManagerUpdate");
        lbm.registerReceiver(updateReceiver, filter);
    }

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (vm != null) vm.initData();
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        lbm.unregisterReceiver(updateReceiver);
    }
}

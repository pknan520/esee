package com.nong.nongo2o.module.dynamic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.FragmentDynamicPublishBinding;
import com.nong.nongo2o.entities.response.DynamicDetail;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.dynamic.activity.DynamicPublishActivity;
import com.nong.nongo2o.module.dynamic.viewModel.DynamicPublishVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-7-1.
 */

public class DynamicPublishFragment extends RxFragment {

    public static final String TAG = "DynamicPublishFragment";
    public static final int GET_AREA = 100;

    private FragmentDynamicPublishBinding binding;
    protected DynamicPublishVM vm;

    private LocalBroadcastManager lbm;

    public static DynamicPublishFragment newInstance() {
        return new DynamicPublishFragment();
    }

    public static DynamicPublishFragment newInstance(Moment dynamic) {
        Bundle args = new Bundle();
        args.putParcelable("dynamic", dynamic);
        DynamicPublishFragment fragment = new DynamicPublishFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            if (getArguments() != null && getArguments().getParcelable("dynamic") != null) {
                vm = new DynamicPublishVM(this, getArguments().getParcelable("dynamic"));
            } else {
                vm = new DynamicPublishVM(this, null);
            }
        }
        registerReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDynamicPublishBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((DynamicPublishActivity) getActivity()).setToolbarTitle("动态发布");

        binding.rvBanner.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        binding.rvDesc.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvDesc.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
        binding.rvDesc.setNestedScrollingEnabled(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        ((DynamicPublishActivity) getActivity()).getTabLayout().setVisibility(View.GONE);
        ((DynamicPublishActivity) getActivity()).setToolbarTitle("动态发布");
    }

    /**
     * 注册广播接受器
     */
    private void registerReceiver() {
        lbm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();
        filter.addAction("selectGoods");
        lbm.registerReceiver(selectGoodsReceiver, filter);
    }

    /**
     * 广播接受器
     */
    private BroadcastReceiver selectGoodsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (vm != null && intent.getSerializableExtra("Goods") != null)
                vm.setSelectedGoods((Goods) intent.getSerializableExtra("Goods"));
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        lbm.unregisterReceiver(selectGoodsReceiver);
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

package com.nong.nongo2o.module.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.FragmentSelectAreaBinding;
import com.nong.nongo2o.entities.CityEntity;
import com.nong.nongo2o.module.common.adapter.CityAdapter;
import com.nong.nongo2o.module.common.viewModel.SelectAreaVM;
import com.nong.nongo2o.module.dynamic.activity.DynamicPublishActivity;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.yokeyword.indexablerv.IndexableLayout;

/**
 * Created by Administrator on 2017-7-1.
 */

public class SelectAreaFragment extends RxFragment {

    public static final String TAG = "SelectAreaFragment";

    private FragmentSelectAreaBinding binding;
    private SelectAreaVM vm;
    private CityAdapter mAdapter;

    public static SelectAreaFragment newInstance() {
        return new SelectAreaFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new SelectAreaVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectAreaBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
//        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (getActivity() instanceof DynamicPublishActivity) {
            ((DynamicPublishActivity) getActivity()).setToolbarTitle("选择城市");
        } else if (getActivity() instanceof AddressMgrActivity) {
            ((AddressMgrActivity) getActivity()).setToolbarTitle("选择城市");
        }

        initCityList();
    }

    private void initCityList() {
        IndexableLayout indexableLayout = binding.indexableLayout;
        indexableLayout.setLayoutManager(new LinearLayoutManager(getActivity()));

        //  setAdapter
        mAdapter = new CityAdapter(getActivity());
        indexableLayout.setAdapter(mAdapter);
        //  setDates
        mAdapter.setDatas(initDates());
        // set Material Design OverlayView
        indexableLayout.setOverlayStyle_MaterialDesign(R.color.colorAccent);
        // 全字母排序。  排序规则设置为：每个字母都会进行比较排序；速度较慢
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
    }

    private List<CityEntity> initDates() {
        List<CityEntity> list = new ArrayList<>();
        List<String> cityStrings = Arrays.asList(getResources().getStringArray(R.array.city_array));
        for (String item : cityStrings) {
            CityEntity city = new CityEntity();
            city.setName(item);
            list.add(city);
        }
        return list;
    }
}

package com.nong.nongo2o.module.common.popup;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.PopupAreaBinding;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.module.common.fragment.SelectAreaListFragment;
import com.nong.nongo2o.module.common.viewModel.PopupAreaVM;
import com.nong.nongo2o.module.common.viewModel.SelectAreaListVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-8-19.
 */

public class AreaPopup extends DialogFragment {

    public static final int AREA_PROVINCE = 0, AREA_CITY = 1, AREA_DISTRICT = 2;

    public static final String TAG = "AreaPopup";

    private PopupAreaBinding binding;
    private PopupAreaVM vm;

    public static AreaPopup newInstance() {
        return new AreaPopup();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new PopupAreaVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        binding = PopupAreaBinding.inflate(inflater, container, false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    /**
     * 初始化地区列表
     */
    private void initView() {
        List<Fragment> fragmentList = new ArrayList<>();
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
//        fragmentList.add(SelectAreaListFragment.newInstance(AREA_PROVINCE, (areaName, level) -> {
//            //  改变当前tab的文字
//            TabLayout.Tab tab = binding.tab.getTabAt(level);
//            if (tab != null) {
//                TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
//                tv.setText(areaName);
//            }
            //  增加一个页面
//            fragmentList.add(SelectAreaListFragment.newInstance(AREA_CITY, (areaName1, level1) -> {
//
//            }));
//            pagerAdapter.notifyDataSetChanged();
            //  增加一个tab

//        }));

        pagerAdapter.notifyDataSetChanged();
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(2);
        binding.tab.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        binding.tab.setupWithViewPager(binding.vp);

        for (int i = 0; i < fragmentList.size(); i++) {
            TabLayout.Tab tab = binding.tab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.item_area_tap);
                if (tab.getCustomView() != null) {
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
                    tv.setText("请选择");
                }
            }
        }
    }
}

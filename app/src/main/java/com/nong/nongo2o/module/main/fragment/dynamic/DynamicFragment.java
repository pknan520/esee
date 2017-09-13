package com.nong.nongo2o.module.main.fragment.dynamic;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.databinding.FragmentDynamicBinding;
import com.nong.nongo2o.module.dynamic.activity.DynamicPublishActivity;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017-6-22.
 */

public class DynamicFragment extends RxFragment{

    public static final int DYNAMIC_FOCUS = 1, DYNAMIC_ALL = 2;
    public static final int PUBLISH_RESULT = 0;

    public static final String TAG = "DynamicFragment";
    private static String[] tabArray = {"关注", "广场", "我的"};

    private FragmentDynamicBinding binding;

    private DynamicMineFragment mineFragment;

    public static DynamicFragment newInstance() {
        return new DynamicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDynamicBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        initToolbar();
        initViewPager();
    }

    private void initToolbar() {
//        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.inflateMenu(R.menu.menu_dynamic);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.publish_dynamic:
                    startActivityForResult(DynamicPublishActivity.newIntent(getActivity()), PUBLISH_RESULT);
                    getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                    break;
            }
            return true;
        });
        getActivity().setTitle("");
        setHasOptionsMenu(true);
    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(DynamicListFragment.newInstance(DYNAMIC_FOCUS));
        fragmentList.add(DynamicListFragment.newInstance(DYNAMIC_ALL));
        mineFragment = DynamicMineFragment.newInstance();
        fragmentList.add(mineFragment);

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(2);
        binding.tab.setupWithViewPager(binding.vp);

        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = binding.tab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.item_main_top_tab);
                if (tab.getCustomView() != null) {
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
                    tv.setText(tabArray[i]);
                }
            }
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.menu_dynamic, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.publish_dynamic:
//                startActivityForResult(DynamicPublishActivity.newIntent(getActivity()), PUBLISH_RESULT);
//                getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PUBLISH_RESULT:
                if (resultCode == RESULT_OK && mineFragment != null) mineFragment.initData();
                break;
        }
    }
}

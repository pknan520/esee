package com.nong.nongo2o.module.main.fragment.dynamic;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.nong.nongo2o.module.common.activity.AddFocusActivity;
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
    private int currentPage = 0;

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
        setHasOptionsMenu(true);
        getActivity().setTitle("");
        binding.toolbar.inflateMenu(R.menu.menu_dynamic);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.publish_dynamic:
                    startActivityForResult(DynamicPublishActivity.newIntent(getActivity()), PUBLISH_RESULT);
                    getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                    break;
                case R.id.menu_add:
                    getActivity().startActivity(AddFocusActivity.newIntent(getActivity()));
                    getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                    break;
            }
            return true;
        });
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
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
//                ((AppCompatActivity)getActivity()).supportInvalidateOptionsMenu();
                binding.toolbar.getMenu().clear();
                switch (position) {
                    case 0:
                    case 1:
                        binding.toolbar.inflateMenu(R.menu.menu_dynamic);
                        break;
                    case 2:
                        binding.toolbar.inflateMenu(R.menu.menu_add);
                        break;
                }
//                ((AppCompatActivity)getActivity()).supportInvalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    public void switchToAll() {
        binding.vp.setCurrentItem(1);
    }

}

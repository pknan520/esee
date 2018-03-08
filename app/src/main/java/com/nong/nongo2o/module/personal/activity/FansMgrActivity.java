package com.nong.nongo2o.module.personal.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityFansMgrBinding;
import com.nong.nongo2o.module.common.activity.AddFocusActivity;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.module.personal.fragment.FansListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-7-17.
 */

public class FansMgrActivity extends RxBaseToolbarActivity {

    public static final int MY_FOCUS = 0, MY_FANS = 1;

    private static String[] tabArray = {"我的关注", "我的粉丝"};

    private ActivityFansMgrBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, FansMgrActivity.class);
    }

    @Override
    protected ViewDataBinding getBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fans_mgr);
        return binding;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setTitle("");

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(FansListFragment.newInstance(MY_FOCUS));
        fragmentList.add(FansListFragment.newInstance(MY_FANS));

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(AddFocusActivity.newIntent(this));
                overridePendingTransition(R.anim.anim_right_in, 0);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

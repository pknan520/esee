package com.nong.nongo2o.module.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.databinding.ActivityMainBinding;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.module.main.fragment.cart.CartFragment;
import com.nong.nongo2o.module.main.fragment.dynamic.DynamicFragment;
import com.nong.nongo2o.module.main.fragment.merchant.MerchantFragment;
import com.nong.nongo2o.module.main.fragment.message.MessageListFragment;
import com.nong.nongo2o.module.main.fragment.personal.PersonalFragment;
import com.nong.nongo2o.module.main.viewModel.MainVM;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RxBaseActivity {

    private static String[] tabArray = {"动态", "口碑", "消息", "购物车", "我的"};
    private static int[] tabPicArray = {R.drawable.selector_main_tab_dynamic, R.drawable.selector_main_tab_merchant,
            R.drawable.selector_main_tap_message, R.drawable.selector_main_tab_cart, R.drawable.selector_main_tab_personal};

    private ActivityMainBinding binding;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainVM mainVM = new MainVM(this);
        initView(binding);
        binding.setViewModel(mainVM);
    }

    private void initView(ActivityMainBinding binding) {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(DynamicFragment.newInstance());
//        fragmentList.add(ShareFragment.newInstance());
        fragmentList.add(MerchantFragment.newInstance());
        fragmentList.add(MessageListFragment.newInstance());
        fragmentList.add(CartFragment.newInstance());
        fragmentList.add(PersonalFragment.newInstance());

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(), fragmentList);
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(1);
        binding.tab.setupWithViewPager(binding.vp);

        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = binding.tab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.item_main_tab);
                if (tab.getCustomView() != null) {
                    ImageView iv = (ImageView) tab.getCustomView().findViewById(R.id.iv);
                    iv.setImageResource(tabPicArray[i]);
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
                    tv.setText(tabArray[i]);
                }
            }
        }

//        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 1 && tab.isSelected() && tab.getCustomView() != null) {
//                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
//                    ImageView iv = (ImageView) tab.getCustomView().findViewById(R.id.iv);
//                    tv.setVisibility(View.GONE);
//                    iv.setImageResource(R.mipmap.ic_launcher);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 1 && tab.isSelected() && tab.getCustomView() != null) {
//                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
//                    ImageView iv = (ImageView) tab.getCustomView().findViewById(R.id.iv);
//                    tv.setVisibility(View.VISIBLE);
//                    iv.setImageBitmap(null);
//                }
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 1 && tab.isSelected()) {
//                    startActivity(SharePublishActivity.newIntent(MainActivity.this));
//                    overridePendingTransition(R.anim.anim_bottom_in, 0);
//                }
//            }
//        });
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

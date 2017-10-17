package com.nong.nongo2o.module.welcome;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.uils.SPUtils;

public class GuideActivity extends AppCompatActivity {

    private GuidePagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Button btnFinish;

    public static Intent newIntent(Context context) {
        return new Intent(context, GuideActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(v -> {
            SPUtils.put(this, "IS_APP_FIRST_OPEN", false);

            startActivity(LoginActivity.newIntent(this, false));
            finish();
            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
        });

        mSectionsPagerAdapter = new GuidePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                btnFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}

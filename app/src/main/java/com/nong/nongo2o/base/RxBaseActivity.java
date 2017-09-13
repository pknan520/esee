package com.nong.nongo2o.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.nong.nongo2o.R;
import com.nong.nongo2o.uils.AppManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * Created by Administrator on 2017-6-21.
 */

public class RxBaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    public void replaceFragment(int containerId, Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            getFragmentManager().beginTransaction()
                    .replace(containerId, fragment, tag)
                    .commit();
        }
    }

    public void switchFragment(int containerId, Fragment from, Fragment to, String tag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.animator.slide_fragment_horizontal_right_in, R.animator.slide_fragment_horizontal_right_out,
                R.animator.slide_fragment_horizontal_right_in, R.animator.slide_fragment_horizontal_right_out);

        if (to.isAdded()) {
            ft.hide(from).show(to).commit();
        } else {
            ft.hide(from).add(containerId, to, tag).addToBackStack(null).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

}

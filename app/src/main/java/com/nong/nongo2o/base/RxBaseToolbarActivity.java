package com.nong.nongo2o.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nong.nongo2o.R;

/**
 * Created by Administrator on 2017-6-21.
 */

public abstract class RxBaseToolbarActivity extends RxBaseActivity {

    protected abstract ViewDataBinding getBinding();

    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) getBinding().getRoot().findViewById(R.id.toolbar);

        if (toolbar == null) {
            throw new IllegalStateException("The subclass of ToolbarActivity must contain a toolbar.");
        }
        setSupportActionBar(toolbar);

        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean canBack() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

}

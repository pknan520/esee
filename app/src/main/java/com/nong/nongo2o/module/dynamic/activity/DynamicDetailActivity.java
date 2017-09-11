package com.nong.nongo2o.module.dynamic.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.databinding.ActivityDynamicDetailBinding;
import com.nong.nongo2o.entities.response.DynamicDetail;
import com.nong.nongo2o.module.dynamic.fragment.DynamicDetailFragment;

/**
 * Created by Administrator on 2017-6-30.
 */

public class DynamicDetailActivity extends RxBaseActivity {

    private ActivityDynamicDetailBinding binding;

    public static Intent newIntent(Context context, DynamicDetail dynamic) {
        Intent intent = new Intent(context, DynamicDetailActivity.class);
        intent.putExtra("dynamic", dynamic);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dynamic_detail);
        initView();
    }

    private void initView() {
        if (getIntent().getParcelableExtra("dynamic") != null) {
            replaceFragment(R.id.fl, DynamicDetailFragment.newInstance(getIntent().getParcelableExtra("dynamic")), DynamicDetailFragment.TAG);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

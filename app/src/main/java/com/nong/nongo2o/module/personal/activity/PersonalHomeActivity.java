package com.nong.nongo2o.module.personal.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseToolbarActivity;
import com.nong.nongo2o.databinding.ActivityPersonalHomeBinding;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.module.personal.fragment.PersonalHomeFragment;

/**
 * Created by Administrator on 2017-6-30.
 */

public class PersonalHomeActivity extends RxBaseActivity {

    private ActivityPersonalHomeBinding binding;

    public static Intent newIntent(Context context, SimpleUser user) {
        Intent intent = new Intent(context, PersonalHomeActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_home);
        initView();
    }

    private void initView() {
        replaceFragment(R.id.fl, PersonalHomeFragment.newInstance((SimpleUser) getIntent().getSerializableExtra("user")), PersonalHomeFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.anim_right_out);
    }
}

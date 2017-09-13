package com.nong.nongo2o.module.main.viewModel;

import android.content.Intent;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.service.InitDataService;

/**
 * Created by Administrator on 2017-9-13.
 */

public class MainVM implements ViewModel {

    private MainActivity activity;

    public MainVM(MainActivity activity) {
        //初始数据
        activity.startService(new Intent(activity, InitDataService.class));
    }
}

package com.nong.nongo2o.module.dynamic.viewModel;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.module.dynamic.fragment.DynamicAddFriendsFragment;

/**
 * Created by Administrator on 2017-7-11.
 */

public class DynamicAddFriendsVM implements ViewModel {

    private DynamicAddFriendsFragment fragment;

    public DynamicAddFriendsVM(DynamicAddFriendsFragment fragment) {
        this.fragment = fragment;
    }
}

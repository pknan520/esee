package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.fragment.SelectAreaFragment;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-1.
 */

public class SelectAreaVM implements ViewModel {

    private SelectAreaFragment fragment;

    public SelectAreaVM(SelectAreaFragment fragment) {
        this.fragment = fragment;
    }

}

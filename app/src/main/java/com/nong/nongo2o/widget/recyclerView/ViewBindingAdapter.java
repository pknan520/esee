package com.nong.nongo2o.widget.recyclerView;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017-7-14.
 */

public class ViewBindingAdapter {

    @BindingAdapter(value = {"setNestedScrollingEnabled"}, requireAll = false)
    public static void setNestedScrollingEnabled(RecyclerView view, boolean enable) {
        view.setNestedScrollingEnabled(enable);
    }
}

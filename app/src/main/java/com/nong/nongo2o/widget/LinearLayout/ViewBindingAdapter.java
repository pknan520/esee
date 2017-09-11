package com.nong.nongo2o.widget.LinearLayout;

import android.databinding.BindingAdapter;

import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;

/**
 * Created by Administrator on 2017-7-14.
 */

public class ViewBindingAdapter {


    @BindingAdapter({"badgeText"})
    public static void setBadgeText(BGABadgeLinearLayout view, String str) {
        view.showTextBadge(str);
    }
}

package com.nong.nongo2o.widget.BadgeView;

import android.databinding.BindingAdapter;

import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * Created by Administrator on 2017-7-13.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"badgeText"})
    public static void setBadgeText(BGABadgeTextView view, String str) {
        if (Integer.parseInt(str) == 0) {
            view.hiddenBadge();
        } else {
            view.showTextBadge(str);
        }
    }

    @BindingAdapter({"llBadgeText"})
    public static void setBadgeText(BGABadgeLinearLayout view, String str) {
        if (Integer.parseInt(str) == 0) view.hiddenBadge();
        else view.showTextBadge(str);
    }
}

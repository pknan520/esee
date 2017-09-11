package com.nong.nongo2o.uils;

import android.content.Context;

/**
 * Created by Administrator on 2017-6-26.
 */

public class ScreenUtils {

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}

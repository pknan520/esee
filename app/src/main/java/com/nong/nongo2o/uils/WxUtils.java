package com.nong.nongo2o.uils;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by Administrator on 2017-10-9.
 */

public class WxUtils {

    //  判断accessToken是否过期
    public static boolean isExpireAccess(Context context) {
        return ((long) SPUtils.get(context, "WX_ACCESS_EXPIRE", 0L)) < Calendar.getInstance().getTimeInMillis();
    }

    //  判断refreshToken是否过期
    public static boolean isExpireRefresh(Context context) {
        return ((long) SPUtils.get(context, "WX_REFRESH_EXPIRE", 0L)) < Calendar.getInstance().getTimeInMillis();
    }
}

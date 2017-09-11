package com.nong.nongo2o.uils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2017-8-25.
 */

public class MyTimeUtils {

    public static String formatTime(long createTime) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long deltaT = currentTime - createTime;
        Log.d("MyTimeUtils", "deltaT = : " + deltaT);

        if (deltaT < 60000) {
            return "刚刚";
        } else if (deltaT < 3600000) {
            return deltaT / 60000 + "分钟前";
        } else if (deltaT < 86400000) {
            return deltaT / 3600000 + "小时前";
        } else if (deltaT < 604800000) {
            return deltaT / 86400000 + "天前";
        } else if (deltaT < 220752000000L){
            return new SimpleDateFormat("MM-dd", Locale.CHINA).format(createTime);
        } else {
            return new SimpleDateFormat("yyyy-MM", Locale.CHINA).format(createTime);
        }
    }
}

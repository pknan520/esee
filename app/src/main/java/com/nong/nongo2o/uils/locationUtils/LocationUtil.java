package com.nong.nongo2o.uils.locationUtils;

import com.baidu.location.BDLocation;

/**
 * Created by Administrator on 2017-5-31.
 */

public class LocationUtil {

    public static boolean isLocationResultEffective(BDLocation bdLocation) {
        return bdLocation != null
                && (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                ||  bdLocation.getLocType() == BDLocation.TypeNetWorkLocation);
    }
}

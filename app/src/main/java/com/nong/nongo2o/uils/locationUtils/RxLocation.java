package com.nong.nongo2o.uils.locationUtils;

import android.content.Context;

import com.baidu.location.BDLocation;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017-8-23.
 */
public class RxLocation
{
    private static RxLocation ourInstance = new RxLocation();

    public static RxLocation getInstance() {
        return ourInstance;
    }

    private RxLocation() {
    }

    public Observable<BDLocation> locate(Context context) {
        return Observable.create(new LocationOnSubscribe(context));
    }
}

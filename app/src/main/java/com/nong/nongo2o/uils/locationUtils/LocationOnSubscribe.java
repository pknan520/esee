package com.nong.nongo2o.uils.locationUtils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2017-8-23.
 */

public class LocationOnSubscribe implements ObservableOnSubscribe<BDLocation> {

    private Context context;

    public LocationOnSubscribe(Context context) {
        this.context = context;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<BDLocation> e) throws Exception {
        BDLocationListener listener = bdLocation -> {
            e.onNext(bdLocation);
            e.onComplete();
        };

        MyLocationClient.get(context).locate(listener);
    }
}

package com.nong.nongo2o.uils.locationUtils;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Administrator on 2017-8-23.
 */

public class MyLocationClient {

    private LocationClient realClient;
    private static volatile MyLocationClient mClient;

    public MyLocationClient(Context context) {
        realClient = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();

        realClient.setLocOption(option);
    }

    public static MyLocationClient get(Context context) {
        if (mClient == null) {
            synchronized (MyLocationClient.class) {
                if (mClient == null) {
                    mClient = new MyLocationClient(context.getApplicationContext());
                }
            }
        }

        return mClient;
    }

    public void locate(BDLocationListener listener) {
        BDLocationListener realListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                listener.onReceiveLocation(bdLocation);
                realClient.unRegisterLocationListener(this);
                stop();
            }
        };

        realClient.registerLocationListener(realListener);

        if (!realClient.isStarted()) {
            realClient.start();
        }
    }

    public void stop() {
        realClient.stop();
    }
}

package com.nong.nongo2o;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nong.nongo2o.uils.Constant;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017-6-21.
 */

public class AdventurerApp extends Application {

    public static AdventurerApp mInstance;
    private IWXAPI wxApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Fresco.initialize(this);
        regToWx();
    }

    private void regToWx() {
        //  通过WXAPIFactory工厂，获得IWXAPI的实例
//        wxApi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        //  将应用的appId注册到微信
//        wxApi.registerApp(Constant.WX_APP_ID);
    }

    public static AdventurerApp getInstance() {
        return mInstance;
    }
}

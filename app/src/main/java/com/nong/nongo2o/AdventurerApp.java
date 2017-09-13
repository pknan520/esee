package com.nong.nongo2o;

import android.app.Application;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nong.nongo2o.service.InitDataService;
import com.nong.nongo2o.uils.Constant;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017-6-21.
 */

public class AdventurerApp extends Application {

    public static AdventurerApp mInstance;
    private IWXAPI wxApi;

    private Set<String> follows;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Fresco.initialize(this);
        regToWx();
        follows = new HashSet<>();
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

    public boolean containFollow(String userCode) {
        return follows.contains(userCode);
    }

    public void addFollow(String userCode) {
        follows.add(userCode);
    }

    public void deleteFollow(String userCode) {
        follows.remove(userCode);
    }

    public void clearFollow() {
        follows.clear();
    }
}

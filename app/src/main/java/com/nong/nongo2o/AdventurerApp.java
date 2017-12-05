package com.nong.nongo2o;

import android.app.Application;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.nong.nongo2o.entity.bean.EaseUserInfo;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.greenDaoGen.EaseUserInfoDao;
import com.nong.nongo2o.service.InitDataService;
import com.nong.nongo2o.uils.Constant;
import com.nong.nongo2o.uils.SPUtils;
import com.nong.nongo2o.uils.dbUtils.DbUtils;
import com.nong.nongo2o.uils.dbUtils.GreenDaoManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017-6-21.
 */

public class AdventurerApp extends Application {

    public static AdventurerApp mInstance;
    public static IWXAPI wxApi;

    public Map<String, Long> sendMsgTime;
    private Set<String> follows;

    private EaseUserInfoDao easeDao;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //  初始化Fresco
        Fresco.initialize(this);
        //  初始化微信
        regToWx();
        //  初始化二维码
        ZXingLibrary.initDisplayOpinion(this);
        //  初始化环信
        easeDao = GreenDaoManager.getInstance().getSession().getEaseUserInfoDao();
        EMOptions emOptions = new EMOptions();
        EMClient.getInstance().init(this, emOptions);
        EMClient.getInstance().setDebugMode(true);
        if (EaseUI.getInstance().init(this, emOptions)) {
            EaseUI.getInstance().setUserProfileProvider(this::getUserInfo);
        }
        //  我关注的人
        follows = new HashSet<>();
    }

    private void regToWx() {
        //  通过WXAPIFactory工厂，获得IWXAPI的实例
        wxApi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        //  将应用的appId注册到微信
        wxApi.registerApp(Constant.WX_APP_ID);
    }

    private EaseUser getUserInfo(String username) {
        EaseUser user = null;

        if (username.equals(EMClient.getInstance().getCurrentUser())) {
            //  本人
            user = new EaseUser(username);
            user.setNick(UserInfo.getInstance().getUserNick());
            user.setAvatar(UserInfo.getInstance().getAvatar());
        } else {
            //  对方
            user = new EaseUser(username);
            EaseUserInfo info = easeDao.load(username);
            if (info != null) {
                user.setNick(info.getUserNick());
                user.setAvatar(info.getAvatar());
            }
        }
        return user;
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

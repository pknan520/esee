package com.nong.nongo2o.uils.dbUtils;

import com.nong.nongo2o.AdventurerApp;
import com.nong.nongo2o.greenDaoGen.DaoMaster;
import com.nong.nongo2o.greenDaoGen.DaoSession;

/**
 * Created by Administrator on 2017-9-19.
 */

public class GreenDaoManager {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager mInstance;

    //单例
    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            //保证异步处理安全操作
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    private GreenDaoManager() {
        if (mInstance == null) {
            DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(AdventurerApp.getInstance(), "city.db", null);
            mDaoMaster = new DaoMaster(openHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
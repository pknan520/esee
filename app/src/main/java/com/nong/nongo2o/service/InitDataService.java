package com.nong.nongo2o.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.nong.nongo2o.AdventurerApp;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-9-13.
 */

public class InitDataService extends Service {

    private static final String TAG = "InitDataService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        
        RetrofitHelper.getFollowAPI()
                .getFollowCodes()
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp != null && !resp.isEmpty()) {
                        AdventurerApp.getInstance().clearFollow();
                        for (String userCode : resp) {
                            AdventurerApp.getInstance().addFollow(userCode);
                        }
                    }
                }, throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}

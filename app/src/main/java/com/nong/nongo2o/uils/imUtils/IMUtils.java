package com.nong.nongo2o.uils.imUtils;

import android.text.TextUtils;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-10-13.
 */

public class IMUtils {

    public static void checkIMLogin(IMCallback imCallback) {
        if (!TextUtils.isEmpty(UserInfo.getInstance().getId()) && !TextUtils.isEmpty(UserInfo.getInstance().getUserCode()) && !EMClient.getInstance().isLoggedInBefore()) {
            EMClient.getInstance().login(String.valueOf(UserInfo.getInstance().getId()), UserInfo.getInstance().getUserCode(), new EMCallBack() {
                @Override
                public void onSuccess() {
                    imCallback.loginCallback(true);
                }

                @Override
                public void onError(int code, String error) {
                    if (code == 204) {
                        // TODO: 2017-10-13 注册环信，注册成功则重新登录一次
                        RetrofitHelper.getAccountAPI()
                                .registerEasemob()
                                .subscribeOn(Schedulers.io())
                                .map(new ApiResponseFunc<>())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(s -> {
                                    EMClient.getInstance().login(String.valueOf(UserInfo.getInstance().getId()), UserInfo.getInstance().getUserCode(), new EMCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            imCallback.loginCallback(true);
                                        }

                                        @Override
                                        public void onError(int code, String error) {
                                            imCallback.loginCallback(false);
                                        }

                                        @Override
                                        public void onProgress(int progress, String status) {

                                        }
                                    });
                                }, throwable -> imCallback.loginCallback(false));

                    } else {
                        imCallback.loginCallback(false);
                    }
                }

                @Override
                public void onProgress(int progress, String status) {

                }
            });
        } else {
            imCallback.loginCallback(true);
        }
    }
}

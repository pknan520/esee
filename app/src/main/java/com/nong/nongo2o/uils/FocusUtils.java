package com.nong.nongo2o.uils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nong.nongo2o.AdventurerApp;
import com.nong.nongo2o.entity.request.CreateFollowRequest;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-9-13.
 */

public class FocusUtils {

    public interface focusCallBack {
        void callback(boolean result);
    }

    public static boolean changeFocus(Context context, boolean isFocus, String targetCode, focusCallBack callback) {
        if (isFocus) {
            //  已经关注了的取消关注
            RetrofitHelper.getFollowAPI()
                    .cancelFocus(targetCode)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        callbackResult(context, isFocus, callback);
                        if (AdventurerApp.getInstance().containFollow(targetCode)) AdventurerApp.getInstance().deleteFollow(targetCode);
                    }, throwable -> {
                        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            //  未关注的关注
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(new CreateFollowRequest(targetCode)));

            RetrofitHelper.getFollowAPI()
                    .addFocus(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        callbackResult(context, isFocus, callback);
                        if (!AdventurerApp.getInstance().containFollow(targetCode)) AdventurerApp.getInstance().addFollow(targetCode);
                    }, throwable -> Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show());
        }


        return false;
    }

    private static void callbackResult(Context context, boolean isFocus, focusCallBack call) {
        Intent intent = new Intent();
        intent.setAction("refreshDynamicList");
        intent.setAction("refreshMerchantList");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        if (call != null) {
            call.callback(!isFocus);
        }
    }

    public static boolean checkIsFocus(String targetCode) {
        return AdventurerApp.getInstance().containFollow(targetCode);
    }
}

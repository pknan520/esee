package com.nong.nongo2o.network.interceptor;

import android.text.TextUtils;

import com.nong.nongo2o.entities.response.User;
import com.nong.nongo2o.entity.bean.UserInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-8-4.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        UserInfo user = UserInfo.getInstance();

        Request request = chain.request().newBuilder()
                .addHeader("sessionToken", TextUtils.isEmpty(user.getSessionToken()) ? "" : user.getSessionToken())
                .build();

        return chain.proceed(request);
    }
}

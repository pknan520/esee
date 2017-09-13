package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-8-4.
 */

public interface AccountService {

    @GET("login")
    Observable<ApiResponse<UserInfo>> login(@Query("openId") String openId);
}

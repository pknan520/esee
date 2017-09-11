package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entities.common.ApiResponse;
import com.nong.nongo2o.entities.response.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-8-4.
 */

public interface AccountService {

    @GET("appLogin")
    Observable<ApiResponse<User>> login(@Query("userCode") String userCode, @Query("password")String password);
}

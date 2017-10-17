package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.UserInfo;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-8-4.
 */

public interface AccountService {

    @GET("login")
    Observable<ApiResponse<UserInfo>> login(@Query("openId") String openId);

    /**
     * 注册
     *
     * @param body CreateUserRequest
     * @return 返回个人资料
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("register")
    Observable<ApiResponse<UserInfo>> register(@Body RequestBody body);

    /**
     * 登出
     *
     * @return 返回操作成功/失败
     */
    @GET("user/logout")
    Observable<ApiResponse<String>> logout();

    /**
     * 认证商家
     *
     * @param body UpdateUserRequest
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/saler")
    Observable<ApiResponse<String>> updateSaler(@Body RequestBody body);

    /**
     * 注册环信
     * @return  返回操作成功/失败
     */
    @GET("user/easemob/register")
    Observable<ApiResponse<String>> registerEasemob();
}

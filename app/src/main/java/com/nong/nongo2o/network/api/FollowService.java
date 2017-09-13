package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.Follow;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-9-13.
 */

public interface FollowService {


    /**
     *
     * @param type 1：我的粉丝；2：我的关注
     * @param page 页码
     * @param pageSize  分页大小
     * @return
     */
    @GET("user/follow/search")
    Observable<ApiResponse<ApiListResponse<Follow>>> userFollowSearch(@Query("type") int type, @Query("page") int page, @Query("pageSize") int pageSize);


    @GET("follow/search")
    Observable<ApiResponse<ApiListResponse<Follow>>> getFollowList();

    /**
     * 查询我所有关注的人的userCode
     *
     * @return 返回userCode列表
     */
    @GET("user/follow/code")
    Observable<ApiResponse<List<String>>> getFollowCodes();

    /**
     * 添加关注
     *
     * @param body 请求体
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/follow")
    Observable<ApiResponse<String>> addFocus(@Body RequestBody body);

    /**
     * 取消关注
     *
     * @param id 对方的userCode
     * @return 返回操作成功/失败
     */
    @DELETE("user/follow")
    Observable<ApiResponse<String>> cancelFocus(@Query("id") String id);
}

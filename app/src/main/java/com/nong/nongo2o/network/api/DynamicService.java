package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entities.common.ApiListResponse;
import com.nong.nongo2o.entities.common.ApiResponse;
import com.nong.nongo2o.entities.common.DynamicComment;
import com.nong.nongo2o.entities.response.DynamicDetail;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-8-22.
 */

public interface DynamicService {

    /**
     * 查询动态列表
     *
     * @param type     1：关注，2：广场，3：我的
     * @param page     页码
     * @param pageSize 每页数量
     * @return 返回动态列表
     */
    @GET("moment/search")
    Observable<ApiResponse<ApiListResponse<DynamicDetail>>> getDynamicList(@Query("type") int type, @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 查询动态详情
     *
     * @param momentCode 动态id
     * @return 返回动态详情
     */
    @GET("moment")
    Observable<ApiResponse<Void>> getDynamicDetail(@Query("momentCode") String momentCode);

    /**
     * 删除动态
     *
     * @param id       动态id
     * @param userCode 用户编码
     * @return 返回操作成功/失败
     */
    @DELETE("moment")
    Observable<ApiResponse<String>> deleteDynamic(@Query("id") String id, @Query("userCode") String userCode);

    /**
     * 点赞
     *
     * @param body 请求体
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("momentfavorite")
    Observable<ApiResponse<String>> likeDynamic(@Body RequestBody body);

    /**
     * 查询动态评论列表
     *
     * @param momentCode 动态id
     * @param page       页码
     * @param pageSize   每页数量
     * @return 返回动态评论列表
     */
    @GET("momentcomment/search")
    Observable<ApiResponse<ApiListResponse<DynamicComment>>> getDynamicCommentList(@Query("momentCode") String momentCode, @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 发表动态评论
     *
     * @param body 请求体
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("momentcomment")
    Observable<ApiResponse<String>> submitDynamicComment(@Body RequestBody body);
}

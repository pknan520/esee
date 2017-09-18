package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.entity.domain.MomentComment;
import com.nong.nongo2o.entity.domain.MomentFavorite;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Observable<ApiResponse<ApiListResponse<Moment>>> getDynamicList(@Query("type") int type, @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 查询某人的动态列表
     *
     * @param type     1：关注，2：广场，3：我的
     * @param userCode 指定的userCode
     * @param page     页码
     * @param pageSize 每页数量
     * @return 返回动态列表
     */
    @GET("moment/search")
    Observable<ApiResponse<ApiListResponse<Moment>>> getUserDynamicList(@Query("type") int type, @Query("userCode") String userCode,
                                                                        @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 发表动态
     *
     * @param body Moment请求体
     * @return 返回成功失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/moment")
    Observable<ApiResponse<String>> postMoment(@Body RequestBody body);

    /**
     * 修改我的动态
     *
     * @param body Moment请求体
     * @return 返回成功失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @PUT("user/moment")
    Observable<ApiResponse<String>> updateDynamic(@Body RequestBody body);

    /**
     * 删除我的动态
     *
     * @param id 动态id
     * @return 返回操作成功/失败
     */
    @DELETE("user/moment")
    Observable<ApiResponse<String>> deleteDynamic(@Query("id") String id);

    /**
     * 查询我的动态列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 返回我的动态列表
     */
    @GET("user/moment/search")
    Observable<ApiResponse<ApiListResponse<Moment>>> getMyDynamicList(@Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 查询点赞列表
     *
     * @param momentCode 动态Code
     * @param page       页码
     * @param pageSize   每页数量
     * @return 返回点赞列表
     */
    @GET("moment/favorite")
    Observable<ApiResponse<ApiListResponse<MomentFavorite>>> getMomentFavoriteList(@Query("momentCode") String momentCode, @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 点赞
     *
     * @param body 请求体
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/moment/favorite")
    Observable<ApiResponse<String>> likeDynamic(@Body RequestBody body);

    /**
     * 取消点赞
     *
     * @param momentCode 动态Code
     * @return 返回操作成功/失败
     */
    @DELETE("user/moment/favorite")
    Observable<ApiResponse<String>> unlikeDynamic(@Query("momentCode") String momentCode);

    /**
     * 发表动态评论
     *
     * @param body 请求体
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/moment/comment")
    Observable<ApiResponse<String>> postDynamicComment(@Body RequestBody body);

    /**
     * 查询动态评论列表
     *
     * @param momentCode 动态code
     * @param page       页码
     * @param pageSize   每页数量
     * @return 返回动态评论列表
     */
    @GET("moment/comment")
    Observable<ApiResponse<ApiListResponse<MomentComment>>> getDynamicCommentList(@Query("momentCode") String momentCode, @Query("page") int page, @Query("pageSize") int pageSize);

}

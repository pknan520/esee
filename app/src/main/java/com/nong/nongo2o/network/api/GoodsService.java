package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.SalerInfo;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.GoodsComment;

import org.reactivestreams.Publisher;

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
 * Created by Administrator on 2017-9-13.
 */

public interface GoodsService {

    /**
     * @param goodsStatus 商品状态 商品状态：0.待发布，1.上架，2.下架
     * @param page        页码
     * @param pageSize    分页大小
     * @return
     */
    @GET("user/goods/search")
    Observable<ApiResponse<ApiListResponse<Goods>>> userGoodsSearch(@Query("goodsStatus") int goodsStatus, @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 获取关注商家列表
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @return 返回商家列表
     */
    @GET("user/saler/search")
    Observable<ApiResponse<ApiListResponse<SalerInfo>>> getFocusSalerInfos(@Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 获取所有商家列表
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @return 返回商家列表
     */
    @GET("search/saler/goods")
    Observable<ApiResponse<ApiListResponse<SalerInfo>>> getAllSalerInfos(@Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 获取某个人的商品列表
     *
     * @param userCode 用户的userCode
     * @param page     页码
     * @param pageSize 每页数量
     * @return 返回商品列表
     */
    @GET("search/goods")
    Observable<ApiResponse<ApiListResponse<Goods>>> getSalerGoods(@Query("userCode") String userCode, @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 新增商品
     *
     * @param body Goods
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/goods")
    Observable<ApiResponse<String>> createUserGoods(@Body RequestBody body);

    /**
     * 更新用户商品
     *
     * @param body UpdateGoodsRequest
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @PUT("user/goods")
    Observable<ApiResponse<String>> updateUserGoods(@Body RequestBody body);

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @DELETE("user/goods")
    Observable<ApiResponse<String>> delUserGoods(@Query("id") String id);


    /**
     * 获取评论列表
     *
     * @param goodsCode 商品Code
     * @return 返回评论列表
     */
    @GET("goods/comment/search")
    Observable<ApiResponse<ApiListResponse<GoodsComment>>> getGoodsComment(@Query("goodsCode") String goodsCode, @Query("starStr") String starStr,
                                                                           @Query("page") int page, @Query("pageSize") int pageSize);


    /**
     * 发表商品评论
     *
     * @param body CreateGoodsCommentRequest
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("/user/goods/comment")
    Observable<ApiResponse<String>> postGoodsComment(@Body RequestBody body);

    /**
     * 获取我买到的商品列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 返回我买到的商品列表
     */
    @GET("user/goods/buyed")
    Observable<ApiResponse<ApiListResponse<Goods>>> getBoughtGoodsList(@Query("page") int page, @Query("pageSize") int pageSize);
}

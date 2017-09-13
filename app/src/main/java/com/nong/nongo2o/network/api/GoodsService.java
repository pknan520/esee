package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.SalerInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-9-13.
 */

public interface GoodsService {

    /**
     *
     * @param goodsStatus 商品状态 商品状态：0.待发布，1.上架，2.下架
     * @param page 页码
     * @param pageSize  分页大小
     * @return
     */
    @GET("user/goods/search")
    Observable<ApiResponse<ApiListResponse<Goods>>> userGoodsSearch(@Query("goodsStatus") int goodsStatus, @Query("page") int page, @Query("pageSize") int pageSize);


    /**
     * 获取关注商家列表
     *
     * @param type 1：关注
     * @return 返回商家列表
     */
    @GET("user/saler/search")
    Observable<ApiResponse<ApiListResponse<SalerInfo>>> getFocusSalerInfos(@Query("type") String type);

    /**
     * 获取所有商家列表
     * @return  返回商家列表
     */
    @GET("search/saler/goods")
    Observable<ApiResponse<ApiListResponse<SalerInfo>>> getAllSalerInfos(@Query("page") int page, @Query("pageSize") int pageSize);
}

package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.WechatPayInfo;
import com.nong.nongo2o.entity.domain.Order;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by HEJF7 on 2017-9-12.
 */

public interface OrderService {

    /**
     * @param orderStatus 0 待支付 1待发货 2待收货 3待评价 4已完成 -1已取消
     * @param userType    0 普通用户 1 商户 2 分销用户（暂时不考虑）
     * @param page        页码
     * @param pageSize    分页大小
     * @return
     */
    @GET("user/order/search")
    Observable<ApiResponse<ApiListResponse<Order>>> userOrderSearch(@Query("orderStatus") Integer orderStatus, @Query("userType") Integer userType, @Query("page") Integer page, @Query("pageSize") Integer pageSize);

    /**
     * 用户下单
     *
     * @param body CreateOrderRequest
     * @return 返回操作成功/失败
     */
    @POST("user/order")
    Observable<ApiResponse<WechatPayInfo>> userOrder(@Body RequestBody body);

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return
     */
    @POST("user/order")
    Observable<ApiResponse<String>> delUserOrder(@Query("id") String id);


}

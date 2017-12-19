package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.WechatPayInfo;
import com.nong.nongo2o.entity.domain.Order;

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
    Observable<ApiResponse<ApiListResponse<Order>>> userOrderSearch(@Query("orderStatusStr") String orderStatusStr, @Query("userType") Integer userType, @Query("page") Integer page, @Query("pageSize") Integer pageSize);

    /**
     * 用户下单
     *
     * @param body CreateOrderRequest
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/order")
    Observable<ApiResponse<Order>> userOrder(@Body RequestBody body);

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 返回操作成功/失败
     */
    @DELETE("user/order")
    Observable<ApiResponse<String>> delUserOrder(@Query("id") String id);

    /**
     * 更新订单
     * @param body  UpdateOrderRequest
     * @return  返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @PUT("user/order")
    Observable<ApiResponse<String>> updateOrder(@Body RequestBody body);

    /**
     * 退款
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("user/refund")
    Observable<ApiResponse<String>> refund(@Body RequestBody body);

    /**
     * 取消支付
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("user/canclepay")
    Observable<ApiResponse<String>> cancelPey(@Body RequestBody body);
}

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
     * 用户订单
     * @param body 传orderStatus、分页信息
     * @return
     */
    @GET("user/order/search")
    Observable<ApiResponse<ApiListResponse<Order>>> userOrderSearch(@Body RequestBody body);

    /**
     * 用户下单
     * @param body
     * @return
     */
    @POST("user/order")
    Observable<ApiResponse<WechatPayInfo>> userOrder(@Body RequestBody body);

    /**
     * 删除订单
     * @param id 订单ID
     * @return
     */
    @POST("user/order")
    Observable<ApiResponse<Void>> delUserOrder(@Query("id") String id);


}

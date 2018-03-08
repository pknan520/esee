package com.nong.nongo2o.network.api;

import android.databinding.ObservableField;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.Cart;

import java.util.List;

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
 * Created by Administrator on 2017-9-14.
 */

public interface CartService {

    /**
     * 获取购物车列表
     *
     * @return 返回购物车列表
     */
    @GET("user/cart/search")
    Observable<ApiResponse<List<Cart>>> getCartList();

    /**
     * 新增购物车
     *
     * @param body CreateCartRequest
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/cart")
    Observable<ApiResponse<String>> postCart(@Body RequestBody body);

    /**
     * 修改购物车（规格、数量）
     *
     * @param body UpdateCartRequest
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @PUT("user/cart")
    Observable<ApiResponse<String>> updateCart(@Body RequestBody body);

    /**
     * 删除购物车
     *
     * @param cartCode 购物车Code
     * @return 返回操作成功/失败
     */
    @DELETE("user/cart")
    Observable<ApiResponse<String>> deleteCart(@Query("id") String cartCode);

    /**
     * 批量删除购物车
     *
     * @param ids 购物车Code
     * @return 返回操作成功/失败
     */
    @DELETE("user/cart")
    Observable<ApiResponse<String>> deleteMultiCart(@Query("ids") String ids);

    /**
     * 批量更新购物车
     *
     * @param body UpdateCartBatchRequest
     * @return 返回操作成功/失败
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @PUT("user/cartbatch")
    Observable<ApiResponse<String>> updateCartList(@Body RequestBody body);
}

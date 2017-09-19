package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.Address;

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
 * Created by HEJF7 on 2017-9-14.
 */

public interface AddressService {

    /**
     * 获取用户收货地址
     *
     * @return
     */
    @GET("user/address/search")
    Observable<ApiResponse<ApiListResponse<Address>>> userAddressSearch(@Query("page") int page, @Query("pageSize") int pageSize);


    /**
     * 新增用户地址
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/address")
    Observable<ApiResponse<String>> userAddress(@Body RequestBody body);

    /**
     * 更新用户地址
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @PUT("user/address")
    Observable<ApiResponse<String>> updateUserAddress(@Body RequestBody body);

    /**
     * 删除用户地址
     *
     * @param id
     * @return
     */
    @DELETE("user/address")
    Observable<ApiResponse<String>> deleteUserAddress(@Query("id") String id);

    /**
     * 获取用户默认地址
     *
     * @return 发挥默认地址
     */
    @GET("user/address/default")
    Observable<ApiResponse<Address>> getDefAddr();
}

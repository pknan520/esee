package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.WechatPayInfo;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017-9-20.
 */

public interface PaymentService {

    /**
     * 获取支付信息
     *
     * @param body CreatePaymentRequest
     * @return 返回支付信息
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"/*,"Accept: application/json"*/})
    @POST("user/pay")
    Observable<ApiResponse<WechatPayInfo>> payWithWechat(@Body RequestBody body);
}

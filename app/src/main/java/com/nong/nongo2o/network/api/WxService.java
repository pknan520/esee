package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.response.WxAccessToken;
import com.nong.nongo2o.entity.response.WxInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-10-9.
 */

public interface WxService {

    /**
     * 获取微信的accesstoken
     *
     * @param appid      应用唯一标识
     * @param secret     微信开发平台的appsecret
     * @param code       微信回调的code
     * @param grant_type 固定为authorization_code
     * @return 返回WxAccessToken
     */
    @GET("sns/oauth2/access_token")
    Observable<WxAccessToken> getWxAccessToken(@Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type);

    /**
     * 刷新或续期access_token使用
     *
     * @param appid         应用唯一标识
     * @param grant_type    refresh_token
     * @param refresh_token 填写通过access_token获取到的refresh_token参数
     * @return 返回WxAccessToken
     */
    @GET("sns/oauth2/refresh_token")
    Observable<WxAccessToken> refreshAccessToken(@Query("appid") String appid, @Query("grant_type") String grant_type, @Query("refresh_token") String refresh_token);

    /**
     * 获取微信个人信息
     *
     * @param access_token 微信的accessToken
     * @param openid       微信用户的openid
     * @return 返回WxInfo
     */
    @GET("sns/userinfo")
    Observable<WxInfo> getWxInfo(@Query("access_token") String access_token, @Query("openid") String openid);
}

package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.App;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

/**
 * Created by Administrator on 2017-10-17.
 */

public interface AppService {

    /**
     * 检查版本
     * @return  返回版本信息
     */
    @GET("app/search")
    Observable<ApiResponse<ApiListResponse<App>>> checkVersion();
}
package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.Follow;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017-9-13.
 */

public interface FollowService {

    @GET("follow/search")
    Observable<ApiResponse<ApiListResponse<Follow>>> getFollowList();

    @GET("user/follow/code")
    Observable<ApiResponse<ApiListResponse<String>>> getFollowCodes();
}

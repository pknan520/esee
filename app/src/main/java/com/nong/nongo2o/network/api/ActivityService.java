package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.Activity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-9-18.
 */

public interface ActivityService {

    /**
     * 获取活动列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 返回活动列表
     */
    @GET("activity/search")
    Observable<ApiResponse<ApiListResponse<Activity>>> getActivities(@Query("page") int page, @Query("pageSize") int pageSize);
}

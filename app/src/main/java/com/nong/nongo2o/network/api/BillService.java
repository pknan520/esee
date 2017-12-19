package com.nong.nongo2o.network.api;

import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.Bill;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-12-12.
 */

public interface BillService {

    /**
     * 获取账单列表
     */
    @GET("user/bill/search")
    Observable<ApiResponse<ApiListResponse<Bill>>> getBillList(@Query("billTypeStr") String billTypeStr, @Query("page") int page);
}

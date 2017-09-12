package com.nong.nongo2o.network.auxiliary;

import com.nong.nongo2o.entity.bean.ApiResponse;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017-6-21.
 */

public class ApiResponseFunc<T> implements Function<ApiResponse<T>, T> {

    @Override
    public T apply(@NonNull ApiResponse<T> apiResponse) throws Exception {
        if (!apiResponse.getCode().equals("0")) throw new ApiException(apiResponse.getMsg());

        if (apiResponse.getData() != null) return apiResponse.getData();

        return (T) apiResponse.getMsg();
    }
}

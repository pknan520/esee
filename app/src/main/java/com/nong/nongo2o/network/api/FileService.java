package com.nong.nongo2o.network.api;


import com.nong.nongo2o.entity.bean.ApiResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017-9-11.
 */

public interface FileService {

    @Multipart
    @POST("user/upload")
    Observable<ApiResponse<String>> uploadFile(@PartMap Map<String, RequestBody> param);

    @Streaming
    @GET
    Observable<ResponseBody> downloadAPK(@Url String url);
}

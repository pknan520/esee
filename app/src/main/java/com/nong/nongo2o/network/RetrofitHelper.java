package com.nong.nongo2o.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.nong.nongo2o.AdventurerApp;
import com.nong.nongo2o.network.api.AccountService;
import com.nong.nongo2o.network.api.ActivityService;
import com.nong.nongo2o.network.api.CartService;
import com.nong.nongo2o.network.api.AddressService;
import com.nong.nongo2o.network.api.DynamicService;
import com.nong.nongo2o.network.api.FileService;
import com.nong.nongo2o.network.api.FollowService;
import com.nong.nongo2o.network.api.GoodsService;
import com.nong.nongo2o.network.api.OrderService;
import com.nong.nongo2o.network.api.UserService;
import com.nong.nongo2o.network.auxiliary.ApiConstants;
import com.nong.nongo2o.network.interceptor.CacheInterceptor;
import com.nong.nongo2o.network.interceptor.HeaderInterceptor;
import com.nong.nongo2o.network.interceptor.LoggerInterceptor;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017-6-21.
 */

public class RetrofitHelper {

    private static OkHttpClient okHttpClient;

    static {
        initOkHttpClient();
    }

    public static FileService getFileAPI() {
        return createApi(FileService.class, ApiConstants.BASE_URL);
    }

    public static AccountService getAccountAPI() {
        return createApi(AccountService.class, ApiConstants.BASE_URL);
    }

    public static UserService getUserAPI() {
        return createApi(UserService.class, ApiConstants.BASE_URL);
    }

    public static DynamicService getDynamicAPI() {
        return createApi(DynamicService.class, ApiConstants.BASE_URL);
    }

    public static FollowService getFollowAPI() {
        return createApi(FollowService.class, ApiConstants.BASE_URL);
    }

    public static GoodsService getGoodsAPI() {
        return createApi(GoodsService.class, ApiConstants.BASE_URL);
    }

    public static CartService getCartAPI() {
        return createApi(CartService.class, ApiConstants.BASE_URL);
    }

    public static AddressService getAddressAPI() {
        return createApi(AddressService.class, ApiConstants.BASE_URL);
    }

    public static ActivityService getActivityAPI() {
        return createApi(ActivityService.class, ApiConstants.BASE_URL);
    }

    public static OrderService getOrderAPI() {
        return createApi(OrderService.class, ApiConstants.BASE_URL);
    }

    /**
     * 根据传入的baseUrl，和api创建retrofit
     */
    private static <T> T createApi(Class<T> clazz, String baseUrl) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        //Register an adapter to manage the date types as long values
        gsonBuilder
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter());

        Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(clazz);
    }

    /**
     * 初始化OKHttpClient,设置缓存,设置超时时间,设置打印日志,设置UA拦截器
     */
    private static void initOkHttpClient() {

        if (okHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (okHttpClient == null) {
                    //设置Http缓存
//                    Cache cache = new Cache(new File(AdventurerApp.getInstance().getCacheDir(), "HttpCache"), 1024 * 1024 * 10);

                    okHttpClient = new OkHttpClient.Builder()
//                            .cache(cache)
//                            .addNetworkInterceptor(new CacheInterceptor())
                            .addNetworkInterceptor(new StethoInterceptor())
                            .addInterceptor(new HeaderInterceptor())
                            .addInterceptor(new LoggerInterceptor("OkHttp", true))
                            .retryOnConnectionFailure(true)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    /**
     * Gson Integer和int类型适配器
     */
    private static class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为int类型,如果后台返回""或者null,则返回0
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }
}

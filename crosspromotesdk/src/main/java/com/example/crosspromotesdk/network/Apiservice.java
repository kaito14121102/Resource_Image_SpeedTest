package com.example.crosspromotesdk.network;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Apiservice {
    @GET("api/v1/list_advertisements")
    Observable<ResponseBody> getAllAdvertisements(@Header("version") String version,@Header("package_name") String lang,@Query("region") String region);
    @Headers("version: ")
    @POST("api/v1/request_advertisement")

    Observable<ResponseBody> postRequestAds(@Header("version") String version,@QueryMap HashMap<String, String> param);
    @Headers("user-key: 9900a9720d31dfd5fdb4352700c")

    @POST("api/v1/click_advertisement")
    Observable<ResponseBody> postClickAds(@Header("version") String version,@QueryMap HashMap<String, String> param);

    @Headers("user-key: 9900a9720d31dfd5fdb4352700c")
    @POST("api/v1/view_advertisement")
    Observable<ResponseBody> postViewAds(@Header("version") String version,@QueryMap HashMap<String, String> param);
}

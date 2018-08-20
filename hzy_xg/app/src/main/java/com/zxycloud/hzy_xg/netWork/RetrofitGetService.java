package com.zxycloud.hzy_xg.netWork;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RetrofitGetService {
    @GET("{action}")
    Observable<ResponseBody> getResult(@Path("action") String action, @HeaderMap Map<String, String> headerParams, @QueryMap Map<String, Object> params);
}

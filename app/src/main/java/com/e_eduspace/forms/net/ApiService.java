package com.e_eduspace.forms.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017-07-19.
 */

public interface ApiService {
    @GET("https://kyfw.12306.cn/otn")
    Observable<ResponseBody> baidu();
}

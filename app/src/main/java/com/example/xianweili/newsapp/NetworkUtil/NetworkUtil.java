package com.example.xianweili.newsapp.NetworkUtil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xianweili on 7/24/18.
 */

public class NetworkUtil {

    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

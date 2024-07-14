package com.example.choresandshop.UserApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {//implements Callback<ResponseBody> {

//    private UserCallback userCallback;
    private static final String BASE_URL = "https://47c8-2a01-73c0-500-e4cb-d8e5-6f31-7a2-e8a2.ngrok-free.app/superapp/";
        //"http://10.0.2.2:8084/superapp/";
//        "http://172.20.10.13:8084/superapp/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

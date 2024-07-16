package com.example.choresandshop.UserApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {//implements Callback<ResponseBody> {

//    private UserCallback userCallback;
    private static final String BASE_URL = "https://37c1-213-57-159-175.ngrok-free.app/superapp/";
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

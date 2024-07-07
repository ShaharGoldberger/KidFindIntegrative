package com.example.choresandshop.UserApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {//implements Callback<ResponseBody> {

//    private UserCallback userCallback;
    private static final String BASE_URL = "https://9c4b-2a01-73c0-601-20be-81ba-d61e-583c-fcb9.ngrok-free.app/superapp/";
        // "http://172.20.10.13:8084/superapp/";
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

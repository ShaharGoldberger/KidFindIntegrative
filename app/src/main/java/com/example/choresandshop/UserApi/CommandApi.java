package com.example.choresandshop.UserApi;

import com.example.choresandshop.boundaries.MiniAppCommandBoundary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommandApi {

    // find objects in square
    @POST("miniapp/{miniAppName}")
    Call<MiniAppCommandBoundary[]> create(
            @Path("miniAppName")String miniAppName,
            @Body MiniAppCommandBoundary boundary
    );
}

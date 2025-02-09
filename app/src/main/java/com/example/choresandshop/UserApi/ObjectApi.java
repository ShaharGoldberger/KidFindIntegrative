package com.example.choresandshop.UserApi;

import com.example.choresandshop.Model.NewUser;
import com.example.choresandshop.Model.Object;
import com.example.choresandshop.Model.User;
import com.example.choresandshop.boundaries.ObjectBoundary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ObjectApi {
    @POST("objects")
    Call<Object> createObject(@Body Object object);

//    @GET("objects/search/byType/{type}")
//    Call<Object[]> getAllChores(
//            @Path("type") String type,
//            @Query("userSuperapp") String userSuperapp,
//            @Query("userEmail") String userEmail,
//            @Query("size") int size,
//            @Query("page") int page
//    );

    @GET("objects/search/byAliasPattern/{pattern}")
    Call<Object[]> getChores(
            @Path("pattern") String pattern,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail,
            @Query("size") int size,
            @Query("page") int page
    );

    // create object
    @POST("objects")
    Call<ObjectBoundary> createObject(@Body ObjectBoundary objectBoundary);

    // update object by id
    @PUT("objects/{superapp}/{id}")
    Call<Void> updateObject(
            @Path("superapp") String superapp,
            @Path("id") String id,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail,
            @Body ObjectBoundary update
    );

    // find object by id
    @GET("objects/{superapp}/{id}")
    Call<ObjectBoundary> findObjectById(
            @Path("superapp")String superapp,
            @Path("email")String email,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail
    );

    // find all objects
    @GET("objects")
    Call<ObjectBoundary[]> findAllObjects(
            @Query("userSuperapp")String userSuperapp,
            @Query("userEmail")String userEmail,
            @Query("size") int size,
            @Query("page") int page
    );

    // find objects by alias
    @GET("objects/superapp/objects/search/byType/{type}")
    Call<ObjectBoundary[]> getObjectsByType(
            @Path("type")String type,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    // find objects by alias
    @GET("objects/search/byAlias/{alias}")
    Call<ObjectBoundary[]> findObjectsByAlias(
            @Path("alias")String alias,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    // find objects by pattern
    @GET("objects/search/byType/{pattern}")
    Call<ObjectBoundary[]> findObjectByPattern(
            @Path("pattern")String pattern,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    // find objects in square
    @GET("objects/search/byLocation/{lat}/{lng}/{distance}")
    Call<ObjectBoundary[]> findObjectInSquare(
            @Path("lat")int lat,
            @Path("lng")int lng,
            @Path("distance")int distance,
            @Query("units") String units,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    // find objects in circle
    @GET("objects/search/byLocation/circle/{lat}/{lng}/{distance}")
    Call<ObjectBoundary[]> findObjectInCircle(
            @Path("lat")int lat,
            @Path("lng")int lng,
            @Path("distance")int distance,
            @Query("units") String units,
            @Query("userSuperapp") String superapp,
            @Query("userEmail") String email,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("objects/{superapp}/{id}")
    Call<Object> getObject(
        @Path("superapp") String superapp,
        @Path("id") String id,
        @Query("userSuperapp") String userSuperapp,
        @Query("userEmail") String userEmail
    );

    @PUT("objects/{superapp}/{id}")
    Call<Void> updateObject(
            @Path("superapp") String superapp,
            @Path("id") String id,
            @Query("userSuperapp") String userSuperapp,
            @Query("userEmail") String userEmail,
            @Body Object object
    );
}

package com.example.admin.Task.rest;

import com.example.admin.Task.apimodel.RestaurantsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by ADMIN on 04-08-2016.
 */
public interface ApiInterface {

    //json?location=51.503186,-0.126446&radius=5000&type=museum&key=YOUR_API_KEY
    @GET("/maps/api/place/nearbysearch/json")
    Call<RestaurantsResponse> getNearByRestros(@QueryMap Map<String, String> options
    );
}

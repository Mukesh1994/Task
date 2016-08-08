package com.example.admin.Task.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ADMIN on 04-08-2016.
 */

public class ApiClient {

    //https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters

    public static final String BASE_URL = "https://maps.googleapis.com";
    private static Retrofit retrofit = null ;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

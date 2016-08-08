package com.example.admin.Task.apimodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by ADMIN on 08-08-2016.
 *///-----------------------------------Viewport.java-----------------------------------


@Generated("org.jsonschema2pojo")
public class Viewport {

    @SerializedName("Northeast")
    @Expose
    private Northeast northeast;
    @SerializedName("southwest")
    @Expose
    private Southwest southwest;

    /**
     * @return The Northeast
     */
    public Northeast getNortheast() {
        return northeast;
    }

    /**
     * @param northeast The Northeast
     */
    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    /**
     * @return The southwest
     */
    public Southwest getSouthwest() {
        return southwest;
    }

    /**
     * @param southwest The southwest
     */
    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

}
package com.example.admin.Task.apimodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by ADMIN on 08-08-2016.
 */

//-----------------------------------Geometry.java-----------------------------------

@Generated("org.jsonschema2pojo")
public class Geometry {

    @SerializedName("location")
    @Expose
    private Location1 location;
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    /**
     * @return The location
     */
    public Location1 getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(Location1 location) {
        this.location = location;
    }

    /**
     * @return The viewport
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * @param viewport The viewport
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}


package com.example.admin.Task.apimodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by ADMIN on 08-08-2016.
 */
//-----------------------------------Photo.java-----------------------------------


@Generated("org.jsonschema2pojo")
public class Photo {

    @SerializedName("height")
    @Expose
    private long height;
    @SerializedName("html_attributions")
    @Expose
    private List<String> htmlAttributions = new ArrayList<String>();
    @SerializedName("photo_reference")
    @Expose
    private String photoReference;
    @SerializedName("width")
    @Expose
    private long width;

    /**
     * @return The height
     */
    public long getHeight() {
        return height;
    }

    /**
     * @param height The height
     */
    public void setHeight(long height) {
        this.height = height;
    }

    /**
     * @return The htmlAttributions
     */
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    /**
     * @param htmlAttributions The html_attributions
     */
    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    /**
     * @return The photoReference
     */
    public String getPhotoReference() {
        return photoReference;
    }

    /**
     * @param photoReference The photo_reference
     */
    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    /**
     * @return The width
     */
    public long getWidth() {
        return width;
    }

    /**
     * @param width The width
     */
    public void setWidth(long width) {
        this.width = width;
    }

}
package com.example.admin.Task.apimodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by ADMIN on 08-08-2016.
 */

//---------------------------------OpeningHours.java-----------------------------------


@Generated("org.jsonschema2pojo")
public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private boolean openNow;
    @SerializedName("weekday_text")
    @Expose
    private List<Object> weekdayText = new ArrayList<Object>();

    /**
     * @return The openNow
     */
    public boolean isOpenNow() {
        return openNow;
    }

    /**
     * @param openNow The open_now
     */
    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    /**
     * @return The weekdayText
     */
    public List<Object> getWeekdayText() {
        return weekdayText;
    }

    /**
     * @param weekdayText The weekday_text
     */
    public void setWeekdayText(List<Object> weekdayText) {
        this.weekdayText = weekdayText;
    }

}

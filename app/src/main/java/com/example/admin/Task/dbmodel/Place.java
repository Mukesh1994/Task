package com.example.admin.Task.dbmodel;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ADMIN on 02-08-2016.
 */

@DatabaseTable(tableName = Place.TABLE_NAME_PLACE)
public class Place {

    public static final String TABLE_NAME_PLACE = "Place";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_lATITUDE = "latitude";
    public static final String FIELD_NAME_LONGITUDE = "longitude";
    public static final String FIELD_NAME_ADDRESS = "address";
    public static final String FIELD_NAME_RATING = "rating";
    public static final String FIELD_NAME_CONTACTNUM = "contact";
    public static final String FIELD_NAME_PRICELEVEL = "pricelevel";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private Long id;

    @DatabaseField(columnName = FIELD_NAME_lATITUDE)
    private double latitude;

    @DatabaseField(columnName = FIELD_NAME_LONGITUDE)
    private double longitude;

    @DatabaseField(columnName = FIELD_NAME_ADDRESS)
    private String address;

    @DatabaseField(columnName = FIELD_NAME_RATING)
    private double rating;

    @DatabaseField(columnName = FIELD_NAME_PRICELEVEL)
    private int priceLevel;

    @DatabaseField(columnName = FIELD_NAME_CONTACTNUM)
    private String contactNum;

    @DatabaseField(columnName = FIELD_NAME)
    private String name;



    //constructor
    public Place() {
    }


    // get and set functions .


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

//    public String getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }
}

package com.example.admin.Task.dbmodel;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ADMIN on 02-08-2016.
 */

@DatabaseTable(tableName = UserLocation.TABLE_NAME_LOCATION)
public class UserLocation {

    public static final String TABLE_NAME_LOCATION = "UserLocation";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_lATITUDE = "latitude";
    public static final String FIELD_NAME_LONGITUDE = "longitude";
    public static final String FIELD_NAME_ADDRESS = "address";
    public static final String FIELD_NAME_TIMESTAMP = "timestamp";


    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private Long id;

    @DatabaseField(columnName = FIELD_NAME_lATITUDE)
    private double latitude;

    @DatabaseField(columnName = FIELD_NAME_LONGITUDE)
    private double longitude;

    @DatabaseField(columnName = FIELD_NAME_ADDRESS)
    private String address;

    @DatabaseField(columnName = FIELD_NAME_TIMESTAMP)
    private String timestamp;

    //constructor
    public UserLocation() {
    }


    // get and set functions .

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getlatitude() {
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

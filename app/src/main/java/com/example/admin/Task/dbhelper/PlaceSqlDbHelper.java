package com.example.admin.Task.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.Task.dbmodel.Place;

import java.util.List;

/**
 * Created by ADMIN on 22-08-2016.
 */
public class PlaceSqlDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_PLACE = "Place";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_lATITUDE = "latitude";
    // coloumns ..
    public static final String FIELD_NAME_LONGITUDE = "longitude";
    public static final String FIELD_NAME_ADDRESS = "address";
    public static final String FIELD_NAME_RATING = "rating";
    public static final String FIELD_NAME_PRICELEVEL = "pricelevel";
    private static final String TAG = "SQLiteHelper";
    /*------------------- Databae Information ----------------------*/
    private static final String DATABASE_NAME = "place.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_PLACE = "CREATE TABLE " + TABLE_NAME_PLACE + "(" +
            FIELD_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FIELD_NAME + " VARCHAR," +
            FIELD_NAME_lATITUDE + " DOUBLE," +
            FIELD_NAME_LONGITUDE + " DOUBLE," +
            FIELD_NAME_ADDRESS + " VARCHAR," +
            FIELD_NAME_RATING + " DOUBLE," +
            FIELD_NAME_PRICELEVEL + "INTEGER" +
            ")";


    public PlaceSqlDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, CREATE_TABLE_PLACE);
        db.execSQL(CREATE_TABLE_PLACE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public long createPlace(Place place) {

        SQLiteDatabase database = this.getWritableDatabase();
        Log.d(TAG, "Creating place in database");
        ContentValues values = new ContentValues();
        Log.d(TAG, "Putting Values"); //Putting Values
        values.put(FIELD_NAME, place.getName());
        values.put(FIELD_NAME_lATITUDE, place.getLatitude());
        values.put(FIELD_NAME_LONGITUDE, place.getLongitude());
        values.put(FIELD_NAME_PRICELEVEL, place.getPriceLevel());
        values.put(FIELD_NAME_ADDRESS, place.getAddress());
        values.put(FIELD_NAME_RATING, place.getRating());

        long insertId = database.insert(TABLE_NAME_PLACE, null, values);
        database.close();
        Log.d(TAG, "Sql db InsertId = " + insertId);
        return insertId;
    }

    public List<Place> getallPlaces() {

        List<Place> places = null;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_PLACE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                place.setLatitude(cursor.getDouble(cursor.getColumnIndex(FIELD_NAME_lATITUDE)));
                place.setLongitude(cursor.getDouble(cursor.getColumnIndex(FIELD_NAME_LONGITUDE)));
                place.setPriceLevel(cursor.getInt(cursor.getColumnIndex(FIELD_NAME_PRICELEVEL)));
                place.setAddress(cursor.getString(cursor.getColumnIndex(FIELD_NAME_ADDRESS)));
                place.setRating(cursor.getDouble(cursor.getColumnIndex(FIELD_NAME_RATING)));
                places.add(place);
            } while (cursor.moveToNext());
        }
        return places;
    }


}






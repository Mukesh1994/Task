package com.example.admin.Task.services;

/**
 * Created by ADMIN on 01-08-2016.
 */

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.admin.Task.dbhelper.UserLocationOpenDatabaseHelper;
import com.example.admin.Task.dbmodel.UserLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class LocationService extends Service implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public final long UPDATE_INTERVAL_IN_MILLISECONDS = 6000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */

    public final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
//    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
//    protected final static String LOCATION_KEY = "location-key";
//    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    final String TAG = "UserLocation update:";
    protected LocationRequest mLocationRequest;

    /**
     * ** Represents a geographical location.
     */

    protected android.location.Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;

    public LocationService() {
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(ActivityRecognition.API)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        Log.i(TAG, "GoogleApiClient Builed");
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused UserLocation Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

//    private String getDirectionsUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//
//        //Sensor enabled
//        String sensor = "sensor=false";
//        String mode = "mode=" + Travel_Mode;
//        Log.d("mode:", mode);
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//        return url ;
//    }

//    public LatLng destination() {
//        TimetableDb db = new TimetableDb(getApplicationContext());
//        course = db.getACourse();
//        LatLng destn = new LatLng(course.getLatitude(), course.getLongitude());
//        return destn;
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


//    public void parse(JSONObject jObject) {
//
//
//        JSONArray jRoutes = null;
//        JSONArray jLegs = null;
//        JSONArray jSteps = null;
//        JSONObject jDistance = null;
//        JSONObject jDuration = null;
//        JSONObject jtime = null;
//        try {
//
//            jRoutes = jObject.getJSONArray("routes");
//
//            //handling error from json response...
//
//
//            /** Traversing all routes */
//            for (int i = 0; i < jRoutes.length(); i++) {
//                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
//                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
//                /** Traversing all legs */
//                for (int j = 0; j < jLegs.length(); j++) {
//
//                    /** Getting distance from the json data */
//                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
//                    HashMap<String, String> hmDistance = new HashMap<String, String>();
//                    hmDistance.put("distance", jDistance.getString("text"));
//
//                    /** Getting duration from the json data */
//                    jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
//                    //total_time = jDuration.getDouble("val");
//
//                    HashMap<String, String> hmDuration = new HashMap<String, String>();
//                    hmDuration.put("duration", jDuration.getString("text"));
//
//                    if (jObject.has("error_message")) {
//                        ETA = "Google Api Query limit Exceeded";
//                        Log.d("error", jObject.getString("error_message"));
//                    } else
//                        ETA = jDuration.getString("text");                          //this is what we hungry for.
//
////                    NotificationCompat.Builder builder =
////                            new NotificationCompat.Builder(this)
////                                    .setSmallIcon(R.drawable.notify)
////                                    .setContentTitle("ETA : " + ETA)
////                                    .setContentText("Next Class:" + course.getCourseTitle())
////                                    .setContentInfo("at:" + course.getTime());
//
//
////                    int NOTIFICATION_ID = 12345;
////
////                    Intent targetIntent = new Intent(this, showclass.class);
////                    targetIntent.putExtra("URL", url);
////                    targetIntent.putExtra("title", course.getCourseTitle());
////                    targetIntent.putExtra("tym", course.getTimestamp());
////                    targetIntent.putExtra("date", course.getDate());
////                    targetIntent.putExtra("time", course.getTime());
////                    targetIntent.putExtra("mode", Travel_Mode);
////                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////                    builder.setContentIntent(contentIntent);
////                    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////                    nManager.notify(NOTIFICATION_ID, builder.build());
//
//                    Log.d(" ETA ", ETA);
//                    Log.d(" course", course_title);
//
//                    /** Adding distance object to the path */
//                    path.add(hmDistance);
//
//                    /** Adding duration object to the path */
//                    path.add(hmDuration);
//
//                }
//
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//        }
//
//
//    }


    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        createLocationRequest();
        startLocationUpdates();
    }


    public void onLocationChanged(android.location.Location location) {

        mCurrentLocation = location;
        Log.d("CurrentLoc", location.getLatitude() + ", " + location.getLongitude());

        //  LatLng origin = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address, city = null, state = null;

        if (addresses != null) {
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            //     String country = addresses.get(0).getCountryName();
            //     postalCode = addresses.get(0).getPostalCode();
            //     String knownName = addresses.get(0).getFeatureName();
        } else
            address = " address not found ";

        String fullAddress = address + " " + city + " " + state;

        Log.d("Address:", fullAddress);

        //inserting to database
        UserLocationOpenDatabaseHelper locnOpenDatabaseHelper = new UserLocationOpenDatabaseHelper(this);

        // creating dao
        Dao<UserLocation, Long> locnDao = locnOpenDatabaseHelper.getDao();

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        UserLocation locn = new UserLocation();
        locn.setLatitude(location.getLatitude());
        locn.setLongitude(location.getLongitude());
        locn.setAddress(fullAddress);
        locn.setTimestamp(timeStamp);

        try {
            locnDao.create(locn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<UserLocation> fetchLocn = null;

        try {
            fetchLocn = locnDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < fetchLocn.size(); i++) {
            locn = fetchLocn.get(i);
            String x = "row " + i + "" + "time : " + locn.getTimestamp() + " address: " + locn.getAddress();
            Log.d("db:", x);
        }


    }


    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        buildGoogleApiClient();
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //Add current time
//
//                Travel_Mode = intent.getStringExtra("activity");
//                if (Travel_Mode.equals("other"))
//                    Travel_Mode = "driving";
//
//                confidence = intent.getExtras().getInt("confidence");
//
//            }
//        };

        //Filter the Intent and register broadcast receiver
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("ImActive");
//        registerReceiver(receiver, filter);
        return START_STICKY;
    }


    /**
     * A class to parse the Google Places in JSON format
     */
}

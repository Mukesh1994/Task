// serviceIntent to get the current location
package com.example.admin.Task.services;
/**
 * Created by ADMIN on 01-08-2016.
 */

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;



public class LocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public final long UPDATE_INTERVAL_IN_MILLISECONDS = 6000;
    public final int STATUS_FINISHED = 1;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */

    public final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    final String TAG = "Place update:";
    protected LocationRequest mLocationRequest;
    /**
     * ** Represents a geographical location.
     */

    protected android.location.Location mCurrentLocation=null;
    private GoogleApiClient mGoogleApiClient;

    public LocationService() {
        super(LocationService.class.getName());
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
     * interval (5 seconds), the Fused Place Provider API returns location updates that are
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
            Log.d(TAG, "permision err");
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

    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission error");
            return;


        }
        createLocationRequest();
        startLocationUpdates();
    }


    public void onLocationChanged(android.location.Location location) {

        mCurrentLocation = location;

        Log.d(TAG,"current locn:" +location.getLatitude() + ", " + location.getLongitude());
        Log.d(TAG,String.valueOf(mCurrentLocation.getLatitude()));
        Intent i = new Intent("locnIntent");
        //Intent i = new Intent()
        i.putExtra("currentLatitude",mCurrentLocation.getLatitude());
        i.putExtra("currentLongitude",mCurrentLocation.getLongitude());
        this.sendBroadcast(i);
        Log.d(TAG,"service stopped");
        stopLocationUpdates();
        this.stopSelf();

        //  LatLng origin = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

      /*
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
        PlaceOrmDbHelper locnOpenDatabaseHelper = new PlaceOrmDbHelper(this);

        // creating dao
        Dao<Place, Long> locnDao = locnOpenDatabaseHelper.getDao();

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        Place locn = new Place();
        locn.setLatitude(location.getLatitude());
        locn.setLongitude(location.getLongitude());
        locn.setAddress(fullAddress);
        locn.setTimestamp(timeStamp);

        try {
            locnDao.create(locn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Place> fetchLocn = null;

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
        */

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
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");
        buildGoogleApiClient();
    }
}

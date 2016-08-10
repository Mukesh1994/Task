package com.example.admin.Task.activities;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.admin.Task.R;
import com.example.admin.Task.adapter.RestrosAdapter;
import com.example.admin.Task.apimodel.RestaurantsResponse;
import com.example.admin.Task.apimodel.Result;
import com.example.admin.Task.rest.ApiClient;
import com.example.admin.Task.rest.ApiInterface;
import com.example.admin.Task.services.LocationService;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import android.content.BroadcastReceiver

//import com.example.admin.Task.apimodel.User;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity Log :";
    public int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION;
    public RecyclerView recyclerView;
    double currntLatitude = 0.0, currntLongitude = 0.0;
    private BroadcastReceiver receiver;
    private boolean showRestros = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            Log.d("LOG::", "permission check done ");
        }


        //  boolean fetchFromDatabase = false ;
        //  if(!fetchFromDatabase)
        // {
        showRestros = true;
        updateCurrentLocn();
        //  }


        // Intent I = new Intent(MainActivity.this, DrawPath.class);
        // startActivity(I);

        //       recyclerView = (RecyclerView) findViewById(R.id.restro_recycler_view);
        //       recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //       showNearbyRestros();


//
////
//        Button clickme=(Button)findViewById(R.id.clickme);
//        clickme.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                something();
//            }
//        });

        // String userName = "mukesh1994";
        //   String locn = "56.6,65&radi" ;
        // String x = "location=51.503186,-0.126446&radius=5000&type=museum&key=AIzaSyC4CHWVYuw-pSQ0dUwO73egdBs1xrSc1kw";
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.503186,-0.126446&radius=500&type=restaurant&key=AIzaSyC4CHWVYuw-pSQ0dUwO73egdBs1xrSc1kw


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //   client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void updateCurrentLocn() {
        Log.d("TAG", "updatingcurrentlocn");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Add current time

                currntLatitude = intent.getDoubleExtra("currentLatitude", 0.0);
                currntLongitude = intent.getDoubleExtra("currentLongitude", 0.0);
                Log.d("TAG", "Got response from service");
                if (showRestros)
                    showNearbyRestros();
            }
        };

        //Filter the Intent and register broadcast receiver
        //IntentFilter fl = new IntentFilter();
        IntentFilter filter = new IntentFilter();
        //filter.set
        filter.addAction("locnIntent");
        registerReceiver(receiver, filter);
        Intent intentService = new Intent(MainActivity.this, LocationService.class);
        startService(intentService);
    }

    public void showNearbyRestros() {

        Log.d(TAG, "Showing REstros");
        String location = currntLatitude + "," + currntLongitude;
        int radius = 5000;
        String type = "restaurant";
        String key = "AIzaSyC4CHWVYuw-pSQ0dUwO73egdBs1xrSc1kw";
        Map<String, String> data = new HashMap<>();
        data.put("location", location);
        data.put("radius", String.valueOf(radius));
        data.put("type", type);
        data.put("key", key);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestaurantsResponse> call = apiService.getNearByRestros(data);
        call.enqueue(new Callback<RestaurantsResponse>() {
            @Override
            public void onResponse(Call<RestaurantsResponse> call, Response<RestaurantsResponse> response) {
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    List<Result> restros = response.body().getResults();
                    recyclerView.setAdapter(new RestrosAdapter(restros, currntLatitude, currntLongitude, R.layout.list_item_restro, getApplicationContext()));

                } else {

                    Log.d("Response : ", response.toString());
                    Log.d("res", call.toString());
                }
            }
            @Override
            public void onFailure(Call<RestaurantsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Response:", t.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.admin.Task.activities/http/host/path")
        //   );
        // AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.admin.Task.activities/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}

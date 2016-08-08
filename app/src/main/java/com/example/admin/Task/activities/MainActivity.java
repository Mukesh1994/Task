package com.example.admin.Task.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.admin.Task.R;
import com.example.admin.Task.adapter.RestrosAdapter;
import com.example.admin.Task.apimodel.RestaurantsResponse;
import com.example.admin.Task.apimodel.Result;
import com.example.admin.Task.rest.ApiClient;
import com.example.admin.Task.rest.ApiInterface;
import com.example.admin.Task.services.LocationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.example.admin.Task.apimodel.User;

public class MainActivity extends AppCompatActivity {

    public int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        boolean toinitiateservice = false ;


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            Log.d("LOG::", "permission check done ");
        }


        if(toinitiateservice){
                Intent S = new Intent(getApplicationContext(), LocationService.class);
                startService(S);
        }

        recyclerView = (RecyclerView) findViewById(R.id.restro_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getNearbyRestros();


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


    }

    public void getNearbyRestros() {

        String location = "19.1032825,72.8485374";
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
                    recyclerView.setAdapter(new RestrosAdapter(restros, R.layout.list_item_restro, getApplicationContext()));
                } else {
                    Log.d("Response : ", response.toString());
                    Log.d("res", call.toString());

                    //   Log.d("res",call.)

                    //Log.d("res",call.execute());
                }
//                 List<Result> restros = .body().getResults();
//                 String name = restros.get(0).getName();
                //RestaurantsResponse response1  = response.body();
                //List<MediaBrowserServiceCompat.Result> res = response1.getResults();
//                 String company = response.body().getCompany();
//                 String location = response.body().getLocation();
//                 int totalrepos = response.body().getPublicRepos();
                //String res = "Name: " + name ;
                //Log.d("Response:",res);
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
}

package com.example.admin.Task.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.admin.Task.R;
import com.example.admin.Task.adapter.RestrosAdapter;
import com.example.admin.Task.apimodel.RestaurantsResponse;
import com.example.admin.Task.apimodel.Result;
import com.example.admin.Task.dbhelper.PlaceOrmDbHelper;
import com.example.admin.Task.dbhelper.PlaceSqlDbHelper;
import com.example.admin.Task.dbmodel.Place;
import com.example.admin.Task.rest.ApiClient;
import com.example.admin.Task.rest.ApiInterface;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity Log :";
    final int radius = 5000;
    final String type = "restaurant";
    final String key = "AIzaSyC4CHWVYuw-pSQ0dUwO73egdBs1xrSc1kw";
    final double sourceLatitude = 19.102512, sourceLongitude = 72.845367;
    final String DbToUse = "ORMLITE";     //write SQL to use sql ..

    public int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION;
    public RecyclerView recyclerView;
    private PlaceOrmDbHelper ormDatabaseHelper = null;
    private PlaceSqlDbHelper sqlDatabaseHelper = null;
    private boolean dbEmpty = false;
    private Dao<Place, Long> placeDao = null;

    // api endpoint - https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.503186,-0.126446&radius=500&type=restaurant&key=AIzaSyC4CHWVYuw-pSQ0dUwO73egdBs1xrSc1kw
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initializing components
        initialize();
        //resolving permission panga .
        permissonCheck();

        //showing restaurants...
        try {
            showRestros();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void permissonCheck() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            Log.d("LOG::", "permission check done ");
        }
    }

    public void initialize() {
        recyclerView = (RecyclerView) findViewById(R.id.restro_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Place> tmp = null;
        if (DbToUse == "SQL")                                             // initializing sql db .
        {
            sqlDatabaseHelper = new PlaceSqlDbHelper(getApplicationContext());
            tmp = sqlDatabaseHelper.getallPlaces();
            if (tmp == null)
                dbEmpty = true;
        } else {
            placeDao = getHelper().getDao();              // initializing ormdb..
            try {
                if (placeDao.countOf() == 0)
                    dbEmpty = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void fillDb(List<Result> data) {

        // placeDao = getHelper().getDao();
        for (int i = 0; i < data.size(); i++) {
            com.example.admin.Task.dbmodel.Place place = new com.example.admin.Task.dbmodel.Place();
            place.setName(data.get(i).getName());
            place.setAddress(data.get(i).getVicinity());
            place.setLatitude(data.get(i).getGeometry().getLocation().getLat());
            place.setLongitude(data.get(i).getGeometry().getLocation().getLng());
            place.setPriceLevel((int) data.get(i).getPriceLevel());
            place.setRating(data.get(i).getRating());

            if (DbToUse == "SQL")
                sqlDatabaseHelper.createPlace(place);                     // use sqldb else ormlite
            else {
                try {
                    placeDao.create(place);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getRestroUsingApiCall() {
        //  Log.d(TAG, "Showing Restros");
        String location = sourceLatitude + "," + sourceLongitude;
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
                    //List<Northeast> xd;
                    InsertInDb task = new InsertInDb();
                    task.execute(restros);
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

    private PlaceOrmDbHelper getHelper() { // DatabaseHelper getHelper() {
        if (ormDatabaseHelper == null) {
            ormDatabaseHelper = OpenHelperManager.getHelper(this, PlaceOrmDbHelper.class);
        }
        return ormDatabaseHelper;
    }

    public void showRestros() throws SQLException {

        if (dbEmpty) {
            Log.d(TAG, "fetching using api");
            getRestroUsingApiCall();
        } else {
            Log.d(TAG, "fetching from db");
            FetchFromDb task = new FetchFromDb();
            task.execute();
        }
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        if (ormDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            ormDatabaseHelper = null;
        }
        //unregisterReceiver(receiver);
    }

    private class InsertInDb extends AsyncTask<List<Result>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<Result>... params) {
            fillDb(params[0]);
            return true;
        }

        protected void onPostExecute(Boolean bool) {
            if (!bool)
                Log.d(TAG, "error inserting db");
            else {
                Log.d(TAG, "SUCCESSFULL INSERTION IN DB");
                FetchFromDb task = new FetchFromDb();
                task.execute();
            }
            //showing on recycler view.

            //     recyclerView.setAdapter(new RestrosAdapter(data, R.layout.list_item_restro, getApplicationContext()));

        }
    }

    private class FetchFromDb extends AsyncTask<Void, Void, List<Place>> {
        @Override
        protected List<Place> doInBackground(Void... params) {
            // PlaceOrmDbHelper placeOpenDatabaseHelper = OpenHelperManager.getHelper(this, PlaceOrmDbHelper.class);

            //Dao<com.example.admin.Task.dbmodel.Place, Long> placeDao = getHelper().getDao();
            List<Place> result = null;
            if (DbToUse == "SQL")
                result = sqlDatabaseHelper.getallPlaces();
            else {
                try {
                    result = placeDao.queryForAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        protected void onPostExecute(List<Place> data) {
            //   List<Place> res =  ;
            recyclerView.setAdapter(new RestrosAdapter(data, R.layout.list_item_restro, getApplicationContext()));
        }
    }
}

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
import com.example.admin.Task.dbhelper.PlaceOpenDatabaseHelper;
import com.example.admin.Task.dbmodel.Place;
import com.example.admin.Task.rest.ApiClient;
import com.example.admin.Task.rest.ApiInterface;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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

    public int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION;
    public RecyclerView recyclerView;
    private PlaceOpenDatabaseHelper databaseHelper = null;
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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    }

    public void fillDb(List<Result> data) {

        Dao<Place, Long> placeDao = getHelper().getDao();
        for (int i = 0; i < data.size(); i++) {
            com.example.admin.Task.dbmodel.Place place = new com.example.admin.Task.dbmodel.Place();
            place.setName(data.get(i).getName());
            place.setAddress(data.get(i).getVicinity());
            place.setLatitude(data.get(i).getGeometry().getLocation().getLat());
            place.setLongitude(data.get(i).getGeometry().getLocation().getLng());
            place.setContactNum("0000000");
            place.setPriceLevel((int) data.get(i).getPriceLevel());
            place.setRating(data.get(i).getRating());
            try {
                placeDao.create(place);
            } catch (SQLException e) {
                e.printStackTrace();
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

    private PlaceOpenDatabaseHelper getHelper() { // DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, PlaceOpenDatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void showRestros() throws SQLException {

        // see if db is empty.
        boolean dbEmpty = false;
        Dao<Place, Long> placedao = getHelper().getDao();
        if (placedao.countOf() == 0)
            dbEmpty = true;

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

    //   public void createDb(List<Result> res) {
//
//        PlaceOpenDatabaseHelper placeOpenDatabaseHelper = OpenHelperManager.getHelper(this, PlaceOpenDatabaseHelper.class);
//        Dao<com.example.admin.Task.dbmodel.Place, Long> placeDao = placeOpenDatabaseHelper.getDao();
//        for (int i = 0; i < res.size(); i++) {
//            com.example.admin.Task.dbmodel.Place place = new com.example.admin.Task.dbmodel.Place();
//            place.setName(res.get(i).getName());
//            place.setAddress(res.get(i).getVicinity());
//            place.setLatitude(res.get(i).getGeometry().getLocation().getLat());
//            place.setLongitude(res.get(i).getGeometry().getLocation().getLng());
//            place.setContactNum("0000000");
//            place.setPriceLevel((int) res.get(i).getPriceLevel());
//            place.setRating(res.get(i).getRating());
//            try {
//                placeDao.create(place);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    //   }

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
        client.connect();
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
        client.disconnect();
    }

    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
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

            // PlaceOpenDatabaseHelper placeOpenDatabaseHelper = OpenHelperManager.getHelper(this, PlaceOpenDatabaseHelper.class);

            Dao<com.example.admin.Task.dbmodel.Place, Long> placeDao = getHelper().getDao();
            List<Place> result = null;
            try {
                result = placeDao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(List<Place> data) {
            //   List<Place> res =  ;
            recyclerView.setAdapter(new RestrosAdapter(data, R.layout.list_item_restro, getApplicationContext()));
        }

    }
}

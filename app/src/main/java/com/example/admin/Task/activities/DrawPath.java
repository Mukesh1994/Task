
package com.example.admin.Task.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.Task.R;
import com.example.admin.Task.services.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawPath extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static String TAG = "DRAWPATH ACTIVITY :";
    private static String API_KEY = "AIzaSyC4CHWVYuw-pSQ0dUwO73egdBs1xrSc1kw";

    private GoogleMap mMap = null;
    private String directionUrl, destination;
    private BroadcastReceiver receiver;
    private double currntLatitude, currntLongitude, destnLatitude, destnLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_path);

        //fetch currntlocation.
        updateCurrentLocn();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onMapLongClick(LatLng latLng) {


    }

    public void updateCurrentLocn() {

        Log.d(TAG, "updatingcurrentlocn");
        //fetching the current location .
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                //getting current loation from service
                currntLatitude = intent.getDoubleExtra("currentLatitude", 0.0);
                currntLongitude = intent.getDoubleExtra("currentLongitude", 0.0);

                //getting destination ..
                destnLatitude = getIntent().getDoubleExtra("destnLatitude", 0.0);
                destnLongitude = getIntent().getDoubleExtra("destnLongitude", 0.0);

                String source = currntLatitude + "," + currntLongitude;
                String destination = destnLatitude + "," + destnLongitude;

                directionUrl = getDirectionApiEndpoint(source, destination);
                // got current location now drawing map.
                Log.d(TAG, directionUrl);
                SupportMapFragment mapFragment =
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(DrawPath.this);

                Log.d("TAG", "Got response from service");
            }
        };

        //Filter the Intent and register broadcast receiver
        IntentFilter filter = new IntentFilter();
        //filter.set
        filter.addAction("locnIntent");
        registerReceiver(receiver, filter);
        Intent intentService = new Intent(DrawPath.this, LocationService.class);
        startService(intentService);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        LatLng currntLocn = new LatLng(currntLatitude, currntLongitude);
        LatLng destnLocn = new LatLng(destnLatitude, destnLongitude);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currntLocn).include(destnLocn);

        //Animate to the bounds
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
        mMap.moveCamera(cameraUpdate);
        mMap.addMarker(new MarkerOptions().position(currntLocn).title("Current Location1"));
        mMap.addMarker(new MarkerOptions().position(destnLocn).title("destination"));
        //path formation on map .
        drawLine();
    }


    public void drawLine() {
        JsonObjectRequest routeRequest = new JsonObjectRequest(Request.Method.GET, directionUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parsing the returned json data .
                ParserTask parser = new ParserTask();
                Log.d("volley Response.:", String.valueOf(response));
                parser.execute(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                VolleyLog.d("connection error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        routeRequest.setRetryPolicy(new DefaultRetryPolicy(40000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getBaseContext()).add(routeRequest);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private String getDirectionApiEndpoint(String source, String destn) {   //https://maps.googleapis.com/maps/api/directions/json?origin=jaipur&destination=mumbai&key=AIzaSyC4CHWVYuw-pSQ0dUwO73egdBs1xrSc1kw
        String baseUrl = "https://maps.googleapis.com/maps/api/directions/json";
        String directionUrl = baseUrl + "?origin=" + source + "&destination=" + destn + "&key=" + API_KEY;
        Log.d("Api_path", directionUrl);
        return directionUrl;
    }

    //parsing jsondata to list .
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        JSONObject jDistance = null;
        JSONObject jDuration = null;
        JSONObject jtime = null;
        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing through  jsonArray - routes */  //
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                /** Traversing through JsongArray - legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    /* Getting distance from the json data */
                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    HashMap<String, String> hmDistance = new HashMap<String, String>();
                    hmDistance.put("distance", jDistance.getString("text"));
                    /** Getting duration from the json data */
                    jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    //total_time = jDuration.getDouble("val");
                    HashMap<String, String> hmDuration = new HashMap<String, String>();
                    hmDuration.put("duration", jDuration.getString("text"));

                    /** Adding distance object to the path */
                    path.add(hmDistance);
                    /** Adding duration object to the path */
                    path.add(hmDuration);

                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing through JsongArray - steps.  */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                }
                routes.add(path);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;
    }


    //idk abt this
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
    // parsing in background..
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(jsonData[0]);
                routes = parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return routes;
        }

        //  formation of line
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }
            // Traversing through the routes
            for (int i = 0; i < result.size(); i++) {

                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

//                    if (j == 0) {    // Get distance from the list
//                        distance = (String) point.get("distance");
//                        continue;72.8587273
//                    } else if (j == 1) { // Get duration from the list
//                        duration = (String) point.get("duration");
//
//                        continue;
//                    }

                    if (j <= 1) continue;

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);

                    Log.d("points: ", position.latitude + "," + position.longitude);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(6);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map ..
            if (mMap != null)
                mMap.addPolyline(lineOptions);
        }
    }
}

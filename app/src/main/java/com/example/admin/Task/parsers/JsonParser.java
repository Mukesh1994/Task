package com.example.admin.Task.parsers;

import android.util.Log;

import com.example.admin.Task.apimodel.Geometry;
import com.example.admin.Task.apimodel.Location1;
import com.example.admin.Task.apimodel.Result;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ADMIN on 23-08-2016.
 */
public class JsonParser {

    private final String TAG = "Parser:";

    public List<Result> parsePlacesApi(JSONObject jObject) {

        JSONArray jResult = null;
        ArrayList<Result> places = new ArrayList<>();

        try {
            jResult = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jResult.length(); i++) {
            Result result = new Result();
            double lat = 0.0, lng = 0.0, rating = 0.0;
            String name = null, address = null;
            //JSONObject jlocn = null ;
            try {
                lat = (double) ((JSONObject) jResult.get(i)).getJSONObject("geometry").getJSONObject("location").get("lat");
                Log.d(TAG, "lat using volley :" + lat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                lng = (double) ((JSONObject) jResult.get(i)).getJSONObject("geometry").getJSONObject("location").get("lng");
                Log.d(TAG, "longt using volley :" + lng);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                rating = (double) ((JSONObject) jResult.get(i)).get("rating");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                name = (String) ((JSONObject) jResult.get(i)).get("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                address = (String) ((JSONObject) jResult.get(i)).get("address");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Geometry geo = new Geometry();
            Location1 locn = new Location1();
            locn.setLat(lat);
            locn.setLng(lng);
            geo.setLocation(locn);
            result.setGeometry(geo);
            result.setName(name);
            result.setRating(rating);
            result.setVicinity(address);
            places.add(result);
        }

        return places;
    }


    public List<List<HashMap<String, String>>> parseDirectionApi(JSONObject jObject) {

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
}

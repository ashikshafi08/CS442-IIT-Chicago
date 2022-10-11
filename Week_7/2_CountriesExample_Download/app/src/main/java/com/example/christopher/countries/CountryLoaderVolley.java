package com.example.christopher.countries;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


public class CountryLoaderVolley {

    private static final String TAG = "CountryLoaderRunnable";
    private static final String DATA_URL = "https://restcountries.com/v3.1/all";

    public static void getSourceData(MainActivity mainActivity) {
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(DATA_URL).buildUpon();
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONArray> listener =
                response -> handleResults(mainActivity, response.toString());

        Response.ErrorListener error = error1 -> {
            Log.d(TAG, "getSourceData: ");
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(new String(error1.networkResponse.data));
                Log.d(TAG, "getSourceData: " + jsonObject);
                handleResults(mainActivity, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, urlToUse,
                        null, listener, error);
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    private static void handleResults(MainActivity mainActivity, String s) {

        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.downloadFailed();
            return;
        }

        final ArrayList<Country> countryList = parseJSON(s);
        if (countryList != null)
            Toast.makeText(mainActivity, "Loaded " + countryList.size() + " countries.", Toast.LENGTH_LONG).show();
        mainActivity.updateData(countryList);
    }

    private static ArrayList<Country> parseJSON(String s) {

        String name;
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                JSONObject nameObj = jCountry.getJSONObject("name");
                name = nameObj.getString("common");

                String capital = "None";
                if (jCountry.has("capital")) {
                    JSONArray capArr = jCountry.getJSONArray("capital");
                    capital = capArr.getString(0);
                }

                Log.d(TAG, "parseJSON: " + name);
                String region = jCountry.getString("region");
                String subRegion = "Other";
                if (jCountry.has("subregion")) {
                    subRegion = jCountry.getString("subregion");
                }

                int population = jCountry.getInt("population");
                int area = jCountry.getInt("area");

                String citizen = "N/A";
                if (jCountry.has("demonyms")) {
                    JSONObject demonymObj = jCountry.getJSONObject("demonyms");
                    JSONObject engObj = demonymObj.getJSONObject("eng");
                    citizen = engObj.getString("m");
                }

                StringBuilder codes = new StringBuilder();
                JSONObject iddObject = jCountry.getJSONObject("idd");
                if (iddObject.has("root")) {
                    String root = iddObject.getString("root");
                    JSONArray suffArr = iddObject.getJSONArray("suffixes");
                    for (int j = 0; j < suffArr.length(); j++) {
                        codes.append(root).append(suffArr.getString(j)).append(" ");
                    }
                } else {
                    codes.append("None");
                }

                StringBuilder borders = new StringBuilder();
                if (!jCountry.has("borders")) {
                    borders.append("None");
                } else {
                    JSONArray jBorders = jCountry.getJSONArray("borders");
                    for (int j = 0; j < jBorders.length(); j++) {
                        borders.append(jBorders.get(j)).append(" ");
                    }
                }
                countryList.add(
                        new Country(name, capital, population, region, subRegion,
                                area, citizen, codes.toString(), borders.toString()));
            }
            Collections.sort(countryList);
            return countryList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

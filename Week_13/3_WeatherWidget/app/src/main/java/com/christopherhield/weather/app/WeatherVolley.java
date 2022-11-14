package com.christopherhield.weather.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class WeatherVolley {

    private static final String TAG = "WeatherVolley";
    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String iconUrl = "https://openweathermap.org/img/w/";
    private static final String yourAPIKey = "6bfc226f3d6885de5b239a8c33047524";

    private static Bitmap bitmap;


    public static void getWeatherData(
            MainActivity mainActivity, String input, boolean fahrenheit) {

        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("units", fahrenheit ? "imperial" : "metric ");
        buildURL.appendQueryParameter("q", input);
        buildURL.appendQueryParameter("appid", yourAPIKey);
        String urlToUse = buildURL.build().toString();


        Response.Listener<JSONObject> listener = response -> {
            try {
                parseJSON(response, mainActivity);
            } catch (Exception e) {
                Log.d(TAG, "getWeatherData: " + e.getMessage());
            }
        };

        Response.ErrorListener error = error1 -> {
            try {
                JSONObject jsonObject = new JSONObject(new String(error1.networkResponse.data));
                Log.d(TAG, "getWeatherData: " + jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void parseJSON(JSONObject jObjMain, MainActivity mainActivity) {
        HashMap<String, String> wData = new HashMap<>();

        try {
            JSONArray weather = jObjMain.getJSONArray("weather");
            JSONObject jWeather = (JSONObject) weather.get(0);
            wData.put("COND", jWeather.getString("main"));
            wData.put("DESC", jWeather.getString("description"));
            String icon = jWeather.getString("icon");

            JSONObject jMain = jObjMain.getJSONObject("main");
            wData.put("TEMP", jMain.getString("temp"));
            wData.put("HUMID", jMain.getString("humidity"));

            JSONObject jWind = jObjMain.getJSONObject("wind");
            wData.put("WIND", jWind.getString("speed"));

            wData.put("CITY", jObjMain.getString("name"));

            JSONObject jSys = jObjMain.getJSONObject("sys");
            wData.put("COUNTRY", jSys.getString("country"));

            long dt = jObjMain.getLong("dt");
            String date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault()).format(new Date(dt * 1000));
            wData.put("DATE", date);

            wData.put("ICON", iconUrl + icon + ".png");

            mainActivity.runOnUiThread(() -> {
                try {
                    mainActivity.updateData(wData);
                } catch (Exception e) {
                    Log.d(TAG, "parseJSON: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "parseJSON: " + e.getMessage());
        }
    }

}

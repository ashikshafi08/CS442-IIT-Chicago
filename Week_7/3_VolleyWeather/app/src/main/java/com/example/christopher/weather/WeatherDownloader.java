package com.example.christopher.weather;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherDownloader {

    private static final String TAG = "WeatherDownloadRunnable";

    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static Weather weatherObj;

    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String iconUrl = "https://openweathermap.org/img/w/";

    //////////////////////////////////////////////////////////////////////////////////
    // Sign up to get your API Key at:  https://home.openweathermap.org/users/sign_up
    private static final String yourAPIKey = "6bfc226f3d6885de5b239a8c33047524";
    //
    //////////////////////////////////////////////////////////////////////////////////

    public static void downloadWeather(MainActivity mainActivityIn,
                                       String city, boolean fahrenheit) {

        mainActivity = mainActivityIn;

        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
        buildURL.appendQueryParameter("q", city);
        buildURL.appendQueryParameter("units", (fahrenheit ? "imperial" : "metric"));
        buildURL.appendQueryParameter("appid", yourAPIKey);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString());

        Response.ErrorListener error =
                error1 -> mainActivity.updateData(null);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

            // "weather" section
            JSONArray weather = jObjMain.getJSONArray("weather");
            JSONObject jWeather = (JSONObject) weather.get(0);
            String condition = jWeather.getString("main");
            String description = jWeather.getString("description");
            String icon = jWeather.getString("icon");

            // "main" section
            JSONObject jMain = jObjMain.getJSONObject("main");
            String temp = jMain.getString("temp");
            String humidity = jMain.getString("humidity");

            // "wind" section
            JSONObject jWind = jObjMain.getJSONObject("wind");
            String wind = jWind.getString("speed");

            // "dt" section
            long dt = jObjMain.getLong("dt");
            String date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault()).format(new Date(dt * 1000));

            // "name" section
            String city = jObjMain.getString("name");

            // "sys" section
            JSONObject jSys = jObjMain.getJSONObject("sys");
            String country = jSys.getString("country");

            weatherObj = new Weather(city, country, condition, description, temp, humidity, wind, date);

            getIcon(icon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getIcon(String icon) {
        String imageURL = iconUrl + icon + ".png";

        Response.Listener<Bitmap> listener = response -> {
            weatherObj.setBitmap(response);
            mainActivity.updateData(weatherObj);
        };

        Response.ErrorListener error = error1 ->
                Log.d(TAG, "getIcon: " + MessageFormat.format(
                        "Image Error: {0}", error1.networkResponse.statusCode));

        ImageRequest imageRequest =
                new ImageRequest(imageURL, listener, 0, 0,
                        ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, error);
        queue.add(imageRequest);
    }
}

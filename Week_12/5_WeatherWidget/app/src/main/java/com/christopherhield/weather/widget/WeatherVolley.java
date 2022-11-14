package com.christopherhield.weather.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherVolley {

    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String iconUrl = "https://openweathermap.org/img/w/";
    private static final String yourAPIKey = "6bfc226f3d6885de5b239a8c33047524";

    private static WeatherWidget weatherWidget;
    private static RequestQueue queue;

    private static Bitmap bitmap;
    private static int temperature;


    public static void getWeatherData(
            Context context, String input, WeatherWidget weatherWidgetIn) {

        weatherWidget = weatherWidgetIn;

        queue = Volley.newRequestQueue(context);

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("units", "imperial");
        buildURL.appendQueryParameter("q", input);
        buildURL.appendQueryParameter("appid", yourAPIKey);
        String urlToUse = buildURL.build().toString();


        Response.Listener<JSONObject> listener = response -> {
            try {

                JSONObject main = response.getJSONObject("main");
                double temp = main.getDouble("temp");
                temperature = (int) temp;

                JSONArray weather = response.getJSONArray("weather");
                String icon = ((JSONObject) weather.get(0)).getString("icon");
                getIcon(icon);

            } catch (Exception e) {
                weatherWidget.handleWeatherError(e.getMessage());
            }
        };

        Response.ErrorListener error = error1 -> {

            try {
                JSONObject jsonObject = new JSONObject(new String(error1.networkResponse.data));
                weatherWidget.handleWeatherError(jsonObject);
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


    private static void getIcon(String icon) {
        String imageURL = iconUrl + icon + ".png";

        Response.Listener<Bitmap> listener = response -> {
            bitmap = response;
            weatherWidget.handleWeatherData(temperature, bitmap);
        };
        Response.ErrorListener error = error1 -> {
            try {
                JSONObject jsonObject = new JSONObject(new String(error1.networkResponse.data));
                weatherWidget.handleWeatherError(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        ImageRequest imageRequest =
                new ImageRequest(imageURL, listener, 0, 0,
                        ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, error);
        queue.add(imageRequest);
    }
}

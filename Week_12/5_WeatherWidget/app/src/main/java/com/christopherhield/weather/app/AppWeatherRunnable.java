package com.christopherhield.weather.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AppWeatherRunnable implements Runnable {

    private static final String TAG = "AppWeatherAsyncTask";
    private final MainActivity mainActivity;
    private final HashMap<String, String> wData = new HashMap<>();
    private Bitmap bitmap;

    private static final String weatherURL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String iconUrl = "http://openweathermap.org/img/w/";
    //////////////////////////////////////////////////////////////////////////////////
    // Sign up to get your API Key at:  https://home.openweathermap.org/users/sign_up
    private static final String yourAPIKey = "6bfc226f3d6885de5b239a8c33047524";
    //
    //////////////////////////////////////////////////////////////////////////////////

    private final String location;
    private final boolean fahrenheit;


    AppWeatherRunnable(MainActivity ma, String location, boolean fahrenheit) {
        mainActivity = ma;
        this.location = location;
        this.fahrenheit = fahrenheit;
    }

    private void handleResults() {
        mainActivity.runOnUiThread(() -> mainActivity.updateData(wData, bitmap));
    }

    @Override
    public void run() { // 0 == city, 1 == fahrenheit

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("q", location);
        buildURL.appendQueryParameter("units", (fahrenheit ? "imperial" : "metric"));
        buildURL.appendQueryParameter("appid", yourAPIKey);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb);

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return;
        }

        parseJSON(sb.toString());

        handleResults();
    }

    private void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

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
            String date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault()).format(new Date(dt*1000));
            wData.put("DATE", date);

            InputStream input = new java.net.URL(iconUrl + icon + ".png").openStream();
            bitmap = BitmapFactory.decodeStream(input);

            Log.d(TAG, "onPostExecute: " + date);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

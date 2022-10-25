package com.example.weatherplay;

import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherDownloader {


    private static final String TAG = "WeatherDownloadRunnable";

    private static MainActivity mainActivity;
    private static DayWeatherActivity dayWeatherActivity;
    private static RequestQueue queue;
    private static RequestQueue newqueue;
    private static DayWeather dayWeatherObj;
    private static Weather weatherObj;
    private static ArrayList<JSONObject> dayArrayList;

    private static final String city = "Chicago";
    private static final String unit = "us";

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline";
    private static final String yourAPIKey = "E3NQPB6RJEB7VD9ZJE7YD7ECW";

    public static void HomedownloadWeather(MainActivity mainActivity , String city ){

        queue = Volley.newRequestQueue(mainActivity);
        // Getting the string
        String urlBuild = weatherURL + "/" + city + "?" + "unitGroup=us" + "&lang=en" + "&key=" + yourAPIKey;

        Response.Listener<JSONObject> listener =
                response -> {
                    parseJSON(response.toString());
                        mainActivity.updateData(weatherObj);

        };

        Response.ErrorListener error =
                error1 -> Toast.makeText(mainActivity, "Error is provoked", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlBuild,
                        null, listener, error);

        queue.add(jsonObjectRequest);
    }

    public static void DaydownloadWeather(DayWeatherActivity dayWeatherActivity , String city ){

        queue = Volley.newRequestQueue(dayWeatherActivity);

        // Getting the string
        String urlBuild = weatherURL + "/" + city + "?" + "unitGroup=us" + "&lang=en" + "&key=" + yourAPIKey;

        Response.Listener<JSONObject> listener =
                response -> {
                    parseJSON(response.toString());
                    dayWeatherActivity.updateData(weatherObj);

                };

        Response.ErrorListener error =
                error1 -> Toast.makeText(dayWeatherActivity, "Error is provoked", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlBuild,
                        null, listener, error);

        queue.add(jsonObjectRequest);
    }




    private static void parseJSON(String s) {

        try {

            ArrayList<JSONObject>  dayArrayList= new ArrayList<>();

            JSONObject jsonObject = new JSONObject(s);

            // The outer
            String address = jsonObject.getString("address");
            // String description = jsonObject.getString("description");



            /// Accessing number of days Array
            JSONArray jsonArrayDays = jsonObject.getJSONArray("days");
            int accum = 0;
            for (int i = 0; i < jsonArrayDays.length(); i++) {
                accum+= 1;
                JSONObject jsonObject1 = jsonArrayDays.getJSONObject(i);


                dayArrayList.add(jsonObject1);

                JSONArray jsonArrayHours = jsonObject1.getJSONArray("hours");

                int accumHours = 0;
                for (int j = 0; j < jsonArrayHours.length(); j++) {
                     accumHours+= 1;

                }
               // Log.d(TAG , "The number of hours " + accumHours);



            }




            // CurrentConditions

            JSONObject jsonObjectcurrentCond = jsonObject.getJSONObject("currentConditions");
            String currentAddress = jsonObject.getString("address");
            String currentDateTimeEpoch = jsonObjectcurrentCond.getString("datetimeEpoch");
            String currentTemp = jsonObjectcurrentCond.getString("temp");
            String currentVisibility = jsonObjectcurrentCond.getString("visibility");
            String currentFeelsLike = jsonObjectcurrentCond.getString("feelslike");
            String currentHumidity = jsonObjectcurrentCond.getString("humidity");
            String currentUvIndex = jsonObjectcurrentCond.getString("uvindex");
            String currentIcon = jsonObjectcurrentCond.getString("icon");
            String currentCloudCover = jsonObjectcurrentCond.getString("cloudcover");
            String currentCondition = jsonObjectcurrentCond.getString("conditions");
            String currentWindSpeed = jsonObjectcurrentCond.getString("windspeed");
            String currentWindDir = jsonObjectcurrentCond.getString("winddir");
            String currentWindGust = jsonObjectcurrentCond.getString("windgust");
            String currentSunriseEpoch = jsonObjectcurrentCond.getString("sunriseEpoch");
            String currentSunsetEpoch = jsonObjectcurrentCond.getString("sunsetEpoch");


            weatherObj  = new Weather(address , currentDateTimeEpoch , currentFeelsLike , currentUvIndex , currentHumidity ,
                    currentIcon , currentVisibility , currentSunsetEpoch , currentSunriseEpoch ,
                    currentCloudCover , currentCondition , currentWindDir , currentWindSpeed , currentWindGust , currentTemp , dayArrayList);


            for (int i = 0; i < dayArrayList.size() ; i++) {
                JSONArray jsonArrayHours = dayArrayList.get(i).getJSONArray("hours");

            }




            // Log.d(TAG, "The number of hours: " + jsonArrayHours.length());


            JSONObject jsonObjectDay1 = (JSONObject) jsonArrayDays.get(1);
            String temp = jsonObjectDay1.getString("temp");
            String datetime = jsonObjectDay1.getString("datetime");

            //Log.d(TAG, temp);
            //Log.d(TAG, datetime);

//            weatherObj = new Weather(address, description, temp, datetime);



            //Log.d(TAG , address);
           // Log.d(TAG , description);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
package com.example.weatherplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DayWeatherActivity extends AppCompatActivity {

    private static final String TAG = "SeconActivity_DailyWeather";
    private static final ArrayList<DayWeather> dayWeatherArrayList= new ArrayList<>();


    private RecyclerView dayDataRecycler;
    private DayWeatherAdapter dayWeatherAdapter;

    TextView datetimeDisplay;
    //ArrayList<DayWeather> dayWeatherArrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_weather);

        dayDataRecycler = findViewById(R.id.dayRecyclerViewId);

        boolean degree= getIntent().getBooleanExtra("degree" , true);
        String city = getIntent().getStringExtra("city");



        doDownload(degree , city);



    }


    public void doDownload(boolean degree , String city){



        Toast.makeText(this, "The city is: " + city , Toast.LENGTH_SHORT);

        WeatherDownloader.DaydownloadWeather(this ,city , degree);
    }


    public String returnTemperature(JSONObject dayJsonObj , int pos){

        for (int i = 0; i < dayJsonObj.length(); i++) {
            try {
                JSONArray hourlyJsonArray  = dayJsonObj.getJSONArray("hours");

                JSONObject hourlyJsonObj = hourlyJsonArray.getJSONObject(pos);

                return hourlyJsonObj.getString("temp");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
            return null;
    }



    public void updateData(Weather weather) {

        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            dayWeatherArrayList.clear();
            // Getting the ArrayList with JSON at first
            ArrayList<JSONObject> weatherDayArrayList = weather.getDayArrayList();
            for (int i = 0; i < weatherDayArrayList.size(); i++) {
                JSONObject dayJsonObject = weatherDayArrayList.get(i);


                String dateTimeEpoch = dayJsonObject.getString("datetimeEpoch");
                String description = dayJsonObject.getString("description");
                String tempMax = dayJsonObject.getString("tempmax");
                String tempMin = dayJsonObject.getString("tempmin");
                String precipprob = dayJsonObject.getString("precipprob");
                String uvIndex = dayJsonObject.getString("uvindex");
                String icon = dayJsonObject.getString("icon");

                // Temp for different day
                String morningTemp = returnTemperature(dayJsonObject, 8);
                String noonTemp = returnTemperature(dayJsonObject, 13);
                String eveTemp = returnTemperature(dayJsonObject, 17);
                String nightTemp = returnTemperature(dayJsonObject, 23);






                dayWeatherArrayList.add(new DayWeather(tempMax, tempMin, description, dateTimeEpoch,
                        precipprob, uvIndex, icon,
                        morningTemp, noonTemp, eveTemp, nightTemp));

            }
            DayWeatherAdapter dayWeatherAdapter = new DayWeatherAdapter(dayWeatherArrayList , DayWeatherActivity.this);
            dayDataRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            dayWeatherAdapter.notifyDataSetChanged();
            dayDataRecycler.setAdapter(dayWeatherAdapter);

        }

         catch (Exception e) {
                e.printStackTrace();
            }




        }





        //TextView addressId = findViewById(R.id.address);
        //TextView currentCloudCover = findViewById(R.id.currentDesc);
        // TextView currentDateTimeEpochId = findViewById(R.id.currentDateTimeEpoch);

    }






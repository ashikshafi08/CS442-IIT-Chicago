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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        boolean degree= getIntent().getBooleanExtra("degree" , true);

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



                String description = dayJsonObject.getString("description");
                String tempMax = dayJsonObject.getString("tempmax");
                String tempMin = dayJsonObject.getString("tempmin");
                String precipprob = dayJsonObject.getString("precipprob");
                String uvIndex = dayJsonObject.getString("uvindex");
                String icon = dayJsonObject.getString("icon");

                // Converting DayTimeEpoch to Viewable string
                long dateTimeLong = Long.parseLong(dayJsonObject.getString("datetimeEpoch"));
                Date dateTime = new Date(dateTimeLong * 1000);
                SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());

                String datetimeEpoch =  dayDate.format(dateTime);
                Log.d(TAG , icon);

                String txtHighLow = tempMax +(degree ? "°F" : "°C" ) + "/" + tempMin + (degree ? "°F" : "°C" );

                // Temp for different day
                String morningTemp = (String.format("%s° %s", returnTemperature(dayJsonObject, 8),(degree ? "F" : "C" )));
                String noonTemp = (String.format("%s° %s", returnTemperature(dayJsonObject, 13),(degree ? "F" : "C" )));
                String eveTemp = (String.format("%s° %s", returnTemperature(dayJsonObject, 17),(degree ? "F" : "C" )));
                String nightTemp = (String.format("%s° %s", returnTemperature(dayJsonObject, 23),(degree ? "F" : "C" )));









                dayWeatherArrayList.add(new DayWeather(txtHighLow ,  description, datetimeEpoch,
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






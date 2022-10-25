package com.example.weatherplay;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static DayWeatherActivity dayWeatherActivity;
    private static Weather weatherObj;
    private static final ArrayList<HourlyWeather> hourlyWeathers = new ArrayList<>();

    private static MainActivity mainActivity;
    private static final String city = "Chicago";
    private static final String unit = "us";

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline";
    private static final String yourAPIKey = "E3NQPB6RJEB7VD9ZJE7YD7ECW";

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button dayActivity = findViewById(R.id.dayActivityButton);


    doDownload();





    }

    public void seconActivity(View view){
        Intent intent = new Intent(this, DayWeatherActivity.class);
        startActivity(intent);
    }



    private void doDownload(){

        WeatherDownloader.HomedownloadWeather(this ,city);
    }

    public void updateData(Weather weather) {
        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView addressId = findViewById(R.id.address);
        TextView currentCloudCover = findViewById(R.id.currentDesc);
        TextView currentDateTimeEpochId = findViewById(R.id.currentDateTimeEpoch);

        Log.d(TAG , weather.getAddress());
        Log.d(TAG , weather.getDatetimeEpoch());

        ArrayList<JSONObject> weatherDayArrayList= weather.getDayArrayList();

//        addressId.setText(String.valueOf(weatherDayArrayList.size()));
//        currentCloudCover.setText(weather.getCloudcover());
//        currentDateTimeEpochId.setText(weather.getDatetimeEpoch());

        // Parsing into the ArrayList and picking up Single JsonObject

            try {
                int accum = 0;
                for (int i = 0; i < 2; i++) {
                    JSONArray hourJsonArray = weatherDayArrayList.get(i).getJSONArray("hours");


                    for (int j = 0; j < hourJsonArray.length(); j++) {
                            JSONObject eachJsonObj = hourJsonArray.getJSONObject(j);
                            accum+= 1;

                            String condition = eachJsonObj.getString("conditions");
                           // Log.d(TAG , condition);

                            String temp = eachJsonObj.getString("temp");
                            //Log.d(TAG , temp);
                    }
                    Log.d(TAG , "The number of hours:"  + accum);

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }






    }









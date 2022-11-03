package com.example.weatherplay;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    // Todo: From here we can record the Fahrenheit Toggler and pass it to the Another intent.

    private static final String TAG = "MainActivity";
    private static DayWeatherActivity dayWeatherActivity;
    private static Weather weatherObj;
    private static final ArrayList<HourlyWeather> hourlyWeatherArrayList = new ArrayList<>();

    private static MainActivity mainActivity;
    public String city = "Chicago, IL";
    private static RecyclerView horizontalRecyclerView;
    private SwipeRefreshLayout swiper;
    private ConstraintLayout constraintLayout;
    private LinearLayout linearInternet,linearNoInternet;

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline";
    private static final String yourAPIKey = "E3NQPB6RJEB7VD9ZJE7YD7ECW";

    private HourlyWeatherAdapter hourlyWeatherAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;



    boolean degreeFarenheit = true;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        linearInternet  = findViewById(R.id.linearInternet);
        linearNoInternet  = findViewById(R.id.linearNoInternet);

        horizontalRecyclerView = findViewById(R.id.horizontalRecyclerView);
        swiper = findViewById(R.id.swiper);

        getData();

        swiper.setOnRefreshListener(this::doRefresh);

//        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult() ,
//                this::handleActivity);
//        hourlyWeatherAdapter = new HourlyWeatherAdapter(hourlyWeatherArrayList, this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        horizontalRecyclerView.setAdapter(hourlyWeatherAdapter);
//        horizontalRecyclerView.setLayoutManager(linearLayoutManager);

        // Log.d(TAG , "Icon" + String.valueOf(returnIcon("clear-day")));

        if (!hasNetworkConnection()){
            linearInternet.setVisibility(View.GONE);
            linearNoInternet.setVisibility(View.VISIBLE);
            swiper.setRefreshing(false);
            Toast.makeText(this, "Connect to a network!" , Toast.LENGTH_SHORT).show();
        }else {
            doDownload(degreeFarenheit , city);
            linearInternet.setVisibility(View.VISIBLE);
            linearNoInternet.setVisibility(View.GONE);
            swiper.setRefreshing(false);
            Toast.makeText(this, "Network Connected!" , Toast.LENGTH_SHORT).show();
        }



    }






    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doDownload(boolean degreeFarenheit , String city){


        if (hasNetworkConnection()){
            WeatherDownloader.HomedownloadWeather(this , city , degreeFarenheit);
        }

//        WeatherDownloader.DaydownloadWeather(dayWeatherActivity.class ,
//                degreeFarenheit);
    }

    public void saveData() {
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putBoolean("FAHRENHEIT", degreeFarenheit);
        editor.putString("city", city);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doRefresh(){

     if (!hasNetworkConnection()){

         linearInternet.setVisibility(View.VISIBLE);
         linearNoInternet.setVisibility(View.GONE);
         swiper.setRefreshing(false);
     }else{
         doDownload(degreeFarenheit , city);

     }
     swiper.setRefreshing(false);

    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(Weather weather) {


        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }

        // ArrayList for the Hourly


        // Getting the each hour in a day data
        ArrayList<JSONObject> weatherDayArrayList= weather.getDayArrayList();

        // Just for the Current Day
        JSONObject currentDayJsonObj = weatherDayArrayList.get(0);

        TextView dateTimeEpochId = findViewById(R.id.datetimeEpochId);
        TextView temp = findViewById(R.id.tempId);
        TextView feelsLike = findViewById(R.id.feelslikeId);
        TextView currentCloud = findViewById(R.id.condCloudCoverId);
        TextView  windPatterns = findViewById(R.id.windAllId);
        TextView humidity = findViewById(R.id.humidityId);
        TextView uvIndex = findViewById(R.id.uvIndexId);
//        TextView visibility = findViewById(R.id.visibillityId);
//
        TextView morningTempId = findViewById(R.id.morningTempId);
        TextView noonTempId = findViewById(R.id.noonTempId);
        TextView eveTempId = findViewById(R.id.eveTempId);
        TextView nightTempId = findViewById(R.id.nightTempId);
        TextView visibilityId = findViewById(R.id.txtVisibility);

        TextView textSunRiseId = findViewById(R.id.txtSunrise);
        TextView textSunSetId = findViewById(R.id.txtSunset);
        ImageView currentWeatherImgId = findViewById(R.id.currentWeatherImg);

        int iconID = returnIcon(weather.getIcon());
        if (iconID != 0) {
            currentWeatherImgId.setImageResource(iconID);

        }else{
            Log.d(TAG, "Cant find the Image COde");
        }


        visibilityId.setText("Visibility: " + weather.getVisibility() + "%");

        String windParams = weather.getWindir() + weather.getWindspeed() + weather.getWindgust();


        String windDir = getDirection(Double.parseDouble(weather.getWindir()));
        windPatterns.setText("Winds: " + windDir + " at " + weather.getWindspeed() + "mph gusting to " + weather.getWindgust() + "mph");

        String condCloud = weather.getConditions() + "(" + weather.getCloudcover() + "% clouds" + ")";

        String morningTemp = returnTemperature(currentDayJsonObj, 8);
        Log.d(TAG, "updateData: "+ morningTemp);
        String noonTemp = returnTemperature(currentDayJsonObj, 13);
        String eveTemp = returnTemperature(currentDayJsonObj, 17);
        String nightTemp = returnTemperature(currentDayJsonObj, 23);

        dateTimeEpochId.setText((dateTimeConvertor(weather.getDatetimeEpoch() , "fullDate")));

        temp.setText(String.format("%s° %s", weather.getTemp(), degreeFarenheit ? "F" : "C"));

        uvIndex.setText(String.format("UV Index %s", weather.getUvindex()));
        humidity.setText(String.format("Humidity: %s", weather.getHumidity()));

        currentCloud.setText(condCloud);

        feelsLike.setText((String.format("Feels like: %s° %s", weather.getFeelslike(), degreeFarenheit ? "F" : "C")));
        morningTempId.setText((String.format("%s° %s", morningTemp, degreeFarenheit ? "F" : "C")));
        noonTempId.setText((String.format("%s° %s", noonTemp, degreeFarenheit ? "F" : "C")));
        eveTempId.setText((String.format("%s° %s", eveTemp, degreeFarenheit ? "F" : "C")));
        nightTempId.setText((String.format("%s° %s", nightTemp, degreeFarenheit ? "F" : "C")));



       String sunRiseText = dateTimeConvertor(weather.getSunriseEpoch() , "timeOnly");
       String sunSetText = dateTimeConvertor(weather.getSunsetEpoch() , "timeOnly");

        textSunRiseId.setText(String.format("Sunrise: %s", sunRiseText));

        textSunSetId.setText(String.format("Sunset: %s", sunSetText));





        long dateTimeEpoch = Long.parseLong(weather.getDatetimeEpoch());
        Date currentDateTimeEpoch = new Date(dateTimeEpoch * 1000);


        SimpleDateFormat fullDate = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy" , Locale.getDefault());
        String fullDateStr = fullDate.format(currentDateTimeEpoch);


            try {
                int accum = 0;
                hourlyWeatherArrayList.clear();
                for (int i = 0; i < 2; i++) {
                    JSONArray hourJsonArray = weatherDayArrayList.get(i).getJSONArray("hours");


                    for (int j = 0; j < hourJsonArray.length(); j++) {
                            JSONObject eachJsonObj = hourJsonArray.getJSONObject(j);
                            accum+= 1;

                            String hourlyDateTimeEpoch = eachJsonObj.getString("datetimeEpoch");
                            String hourlyIcon = eachJsonObj.getString("icon");
                            String hourlyTemp = eachJsonObj.getString("temp");

                             // Log.d(TAG , "Hourly Temp: " + hourlyTemp);
                            String hourlyCond = eachJsonObj.getString("conditions");

                            long hourlyDateTime = Long.parseLong(hourlyDateTimeEpoch);
                            //Log.d(TAG , hourlyDateTimeEpoch);
                            Date hourlyDate = new Date(hourlyDateTime * 1000);

                            SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a" , Locale.getDefault());
                            timeOnly.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
                            SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd" , Locale.getDefault());

                            String timeonlyStr = timeOnly.format(hourlyDate);

                            String dayDateStr = dayDate.format(hourlyDate);
                        hourlyWeatherArrayList.add(new HourlyWeather(dayDateStr , timeonlyStr , hourlyIcon , hourlyTemp , hourlyCond));

                        Log.d(TAG , "Size: " + hourlyWeatherArrayList.size());

                        //Log.d(TAG , "Temp: " + hourlyTemp);


                    }
                    HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(hourlyWeatherArrayList, this);
                    //Log.d(TAG , "Value from the ArrayList: " + updatedObj.getTemp());

                    //Log.d(TAG , "Notify");
                    // horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false));
                    horizontalRecyclerView.setAdapter(hourlyWeatherAdapter);
                    //doDownload(degreeFarenheit);





                }






            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }


    public String dateTimeConvertor(String dateTimeEpochStr , String choice){
        /** Gives the Date Time in desired format
         * choice -> Is the type of format we want the output to be
         *
         * fullDate -> Thu Sep 29 12:00 AM, 2022
         * timeOnly -> 12:00 AM
         * dayDate -> Thursday 09/29
         */



        long dateTimeEpoch = Long.parseLong(dateTimeEpochStr);
        Date dateTime = new Date(dateTimeEpoch * 1000);
        if (choice == "fullDate"){
            SimpleDateFormat fullDate =
                    new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());

            return fullDate.format(dateTime);
        }else if(choice == "timeOnly"){
            SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());

            return timeOnly.format(dateTime);
        }else if (choice == "dayDate"){

            SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
            return dayDate.format(dateTime);
        }

        return null;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        MenuItem item = menu.getItem(0);
        if(degreeFarenheit){
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
        }
        else{
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_c));
        }
        return true;


    }





//    public void checkNetwork(){
//        if(hasNetworkConnection()){
//            linearInternet.setVisibility(View.VISIBLE);
//            linearNoInternet.setVisibility(View.GONE);
//        }
//        else{
//            linearInternet.setVisibility(View.GONE);
//            linearNoInternet.setVisibility(View.VISIBLE);
//        }
//    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    public void openDailyActivity() {
        Intent intent = new Intent(this, DayWeatherActivity.class);
        startActivity(intent);
    }


    public int returnIcon(String icon){

        icon = icon.replace("-" , "_");
        int iconId = MainActivity.this.getResources().getIdentifier(icon , "drawable" , MainActivity.this.getPackageName());

        // Error Handling
        if (iconId ==0){
            Log.d(TAG , "parseCurrentRecord: CANNOT FIND ICON" + icon);
            return 0;
        }
        return iconId;
    }

    private void getData() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        degreeFarenheit = prefs.getBoolean("FAHRENHEIT", true);
        city = prefs.getString("city", "Chicago, Illinois");

        Objects.requireNonNull(getSupportActionBar()).setTitle("" + city);
    }






        @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.dailyMenu) {

            // openDailyActivity();
            Intent intent = new Intent(this, DayWeatherActivity.class);
            intent.putExtra("city" , city);
            intent.putExtra("degree" , degreeFarenheit);
            startActivity(intent);
            return true;

        }
        else if (item.getItemId() == R.id.unitsMenu) {
            Intent data = new Intent(this, DayWeatherActivity.class);
            if (degreeFarenheit){ // its True here
                Toast.makeText(this, "Degree Farhenheit" + degreeFarenheit , Toast.LENGTH_SHORT).show();
                degreeFarenheit = false;

                data.putExtra("degreeFarenheit" , degreeFarenheit);
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_c));
                doDownload(degreeFarenheit, city);
                saveData();
                Toast.makeText(this, "The degree: " + degreeFarenheit , Toast.LENGTH_SHORT).show();

            }else{
               // Toast.makeText(this, "Degree Farhenheit" + degreeFarenheit , Toast.LENGTH_SHORT);
                degreeFarenheit = true;
                data.putExtra("degreeFarenheit" , degreeFarenheit);
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
                doDownload(degreeFarenheit , city);
                saveData();
                Toast.makeText(this, "The degree: " + degreeFarenheit , Toast.LENGTH_SHORT).show();

            }

           //  Intent data = new Intent(this, DayWeatherActivity.class);
            //data.putExtra("degreeFarenheit" , degreeFarenheit);
            return true;

        }

        else if (item.getItemId() == R.id.locationMenu) {

            if(hasNetworkConnection()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Create an edittext and set it to be the builder's view
                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);

                builder.setTitle("Enter Location");
                builder.setMessage("For US Location, enter as 'City', or 'City, State'\n For international  locations enter as 'City,Country'");


                // lambda can be used here (as is below)
                builder.setPositiveButton("OK", (dialog, id) -> {
                    city = et.getText().toString().trim();
                    doDownload(degreeFarenheit , city);


                });

                // lambda can be used here (as is below)
                builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Toast.makeText(getApplicationContext(),"This function requires devices to be connected to the internet",Toast.LENGTH_LONG).show();
            }

            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }




}















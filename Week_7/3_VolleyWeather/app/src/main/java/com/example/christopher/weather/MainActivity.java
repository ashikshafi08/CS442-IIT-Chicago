package com.example.christopher.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Don't forget you need:
    //      <uses-permission android:name="android.permission.INTERNET"/>

    private static final String TAG = "MainActivity";
    private boolean fahrenheit = true;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (!sharedPref.contains("FAHRENHEIT")) {
            editor.putBoolean("FAHRENHEIT", true);
            editor.apply();
        }

        fahrenheit = sharedPref.getBoolean("FAHRENHEIT", true);
        if (fahrenheit)
            ((RadioButton) findViewById(R.id.fDegrees)).setChecked(true);
        else
            ((RadioButton) findViewById(R.id.cDegrees)).setChecked(true);


        doDownload();
    }

    public void doNewTemp(View v) {
        doDownload();
    }

    private void doDownload() {
        EditText et = findViewById(R.id.city);
        String cityName = et.getText().toString().trim().replaceAll(", ", ",");

        WeatherDownloader.downloadWeather(this, cityName, fahrenheit);
    }

    public void tempSet(View v) {
        if (v.getId() == R.id.fDegrees) {
            fahrenheit = true;
            editor.putBoolean("FAHRENHEIT", true);
        } else if (v.getId() == R.id.cDegrees) {
            fahrenheit = false;
            editor.putBoolean("FAHRENHEIT", false);
        } else {
            Log.d(TAG, "tempSet: Unexpected temp units selected");
            return;
        }
        editor.apply();
        doDownload();
    }

    public void updateData(Weather weather) {
        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView city = findViewById(R.id.city);
        city.setText(String.format("%s, %s", weather.getCity(), weather.getCountry()));

        TextView temp = findViewById(R.id.temp);
        temp.setText(String.format("%.0f° " + (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getTemp())));

        TextView condition = findViewById(R.id.condition);
        condition.setText(weather.getConditions());

        TextView description = findViewById(R.id.description);
        description.setText(String.format("(%s)", weather.getDescription()));

        TextView date = findViewById(R.id.date);
        date.setText(String.format("%s (%s°)", weather.getDate(), weather.getTemp()));

        TextView wind = findViewById(R.id.wind);
        wind.setText(String.format("Wind: %.0f " + (fahrenheit ? "mph" : "mps"), Double.parseDouble(weather.getWind())));

        TextView humid = findViewById(R.id.humidity);
        humid.setText(String.format(Locale.getDefault(), "Humidity: %.0f%%", Double.parseDouble(weather.getHumidity())));

        ImageView image = findViewById(R.id.imageView);
        image.setImageBitmap(weather.getBitmap());
    }


}

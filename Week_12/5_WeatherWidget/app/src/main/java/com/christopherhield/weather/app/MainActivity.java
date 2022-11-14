package com.christopherhield.weather.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.christopherhield.weather.widget.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private boolean fahrenheit = true;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("WEATHER_SETTINGS", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (!sharedPref.contains("TEMP_UNIT")) {
            editor.putString("TEMP_UNIT", "FAHRENHEIT");
            editor.apply();
            fahrenheit = true;
        } else {
            String unit = sharedPref.getString("TEMP_UNIT", "FAHRENHEIT");
            fahrenheit = unit.equals("FAHRENHEIT");
        }
        editor.apply();


        if (fahrenheit)
            ((RadioButton) findViewById(R.id.fDegrees)).setChecked(true);
        else
            ((RadioButton) findViewById(R.id.cDegrees)).setChecked(true);


        doAsyncLoad();

    }


    public void doNewTemp(View v) {
        doAsyncLoad();
    }

    private void doAsyncLoad() {
        EditText et = findViewById(R.id.city);
        String cityName = et.getText().toString().trim().replaceAll(", ", ",");

        AppWeatherRunnable awr =
                new AppWeatherRunnable(this, cityName, fahrenheit);
        new Thread(awr).start();
    }


    public void tempSet(View v) {
        if (v.getId() == R.id.fDegrees) {
            fahrenheit = true;
            editor.putString("TEMP_UNIT", "FAHRENHEIT");
        } else {
            fahrenheit = false;
            editor.putString("TEMP_UNIT", "CELSIUS");
        }
        editor.apply();
        doAsyncLoad();
    }

    public void updateData(HashMap<String, String> wData, Bitmap bitmap) {
        if (wData.isEmpty()) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView city = findViewById(R.id.city);
        city.setText(String.format("%s, %s", wData.get("CITY"), wData.get("COUNTRY")));

        TextView temp = findViewById(R.id.temp);
        temp.setText(String.format("%.0f° " + (fahrenheit ? "F" : "C"), Double.parseDouble(Objects.requireNonNull(wData.get("TEMP")))));

        TextView condition = findViewById(R.id.condition);
        condition.setText(wData.get("COND"));

        TextView description = findViewById(R.id.description);
        description.setText(String.format("(%s)", wData.get("DESC")));

        TextView date = findViewById(R.id.date);
        date.setText(String.format("%s (%s°)", wData.get("DATE"), wData.get("TEMP")));

        TextView wind = findViewById(R.id.wind);
        wind.setText(String.format("Wind: %.0f " + (fahrenheit ? "mph" : "mps"), Double.parseDouble(Objects.requireNonNull(wData.get("WIND")))));

        TextView humid = findViewById(R.id.humidity);
        humid.setText(String.format(Locale.getDefault(), "Humidity: %.0f%%", Double.parseDouble(Objects.requireNonNull(wData.get("HUMID")))));

        ImageView image = findViewById(R.id.imageView);
        image.setImageBitmap(bitmap);
    }

}

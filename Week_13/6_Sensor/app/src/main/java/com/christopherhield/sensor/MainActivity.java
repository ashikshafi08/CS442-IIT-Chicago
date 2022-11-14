package com.christopherhield.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView azimuthText, pitchText, rollText;
    private SensorManager sManager;
    private SensorEventListener mySensorEventListener;

    // Gravity rotational data
    private float[] gravity;

    // Magnetic rotational data
    private float[] magnetic;

    // azimuth, pitch and roll
    private float azimuth; //Device rotation
    private float pitch; // Device tilt up-down
    private float roll; // Device tilt left-right


    // Note that app orientation is locked to portrait

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        azimuthText = findViewById(R.id.azimuth);
        pitchText = findViewById(R.id.pitch);
        rollText = findViewById(R.id.roll);

        setupSensor();
    }

    private void setupSensor() {

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mySensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Nothing to do here
            }
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                    gravity = event.values;
                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                    magnetic = event.values;
                if (gravity != null && magnetic != null) {
                    float[] R = new float[9];
                    float[] I = new float[9];
                    boolean success = SensorManager.getRotationMatrix(R, I, gravity, magnetic);
                    if (success) {
                        float[] orientation = new float[3];
                        SensorManager.getOrientation(R, orientation);
                        azimuth = orientation[0]; // orientation contains: azimut, pitch and roll
                        pitch = orientation[1];
                        roll = orientation[2];
                        azimuthText.setText(String.format(Locale.US, "%.0f°", Math.toDegrees(azimuth)));
                        pitchText.setText(String.format(Locale.US, "%.0f°", Math.toDegrees(pitch)));
                        rollText.setText(String.format(Locale.US, "%.0f°", Math.toDegrees(roll)));
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        sManager.registerListener(mySensorEventListener,
                sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener,
                sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();
    }

    @Override
    protected void onPause() {
        sManager.unregisterListener(mySensorEventListener);
        super.onPause();
    }
}

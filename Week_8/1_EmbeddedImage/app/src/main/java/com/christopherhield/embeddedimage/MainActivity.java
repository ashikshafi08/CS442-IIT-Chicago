package com.christopherhield.embeddedimage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        RadioGroup radioGroup1 = findViewById(R.id.radioGroup1);
        RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);

        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.centerInside)
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            else if (checkedId == R.id.fitStart)
                imageView.setScaleType(ImageView.ScaleType.FIT_START);
            else if (checkedId == R.id.fitEnd)
                imageView.setScaleType(ImageView.ScaleType.FIT_END);
            else if (checkedId == R.id.center)
                imageView.setScaleType(ImageView.ScaleType.CENTER);
        });

        radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.fitXY)
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            else if (checkedId == R.id.fitCenter)
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            else if (checkedId == R.id.centerCrop)
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        });

    }
}
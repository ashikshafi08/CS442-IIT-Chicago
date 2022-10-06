package com.example.activitiesintents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityA extends AppCompatActivity {

    private TextView textActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        textActivityId = findViewById(R.id.textActivity);

        // Here we use the getIntent, from which Intent did it triggered from?
        // Intent that started them
        Intent intent = getIntent();

        if(intent.hasExtra("OPENING_CLASS")){
            String text = intent.getStringExtra("OPENING_CLASS");
            if (text.isEmpty()){
                textActivityId.setText("Text is empty");
            }
            textActivityId.setText(String.format("ActivityA\n opened from %s" , text));
        }
    }




}
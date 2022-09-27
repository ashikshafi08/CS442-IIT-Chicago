package com.example.christopher.activities;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView userText;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userText = findViewById(R.id.userText);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
    }

    public void doA(View v) {
        Intent intent = new Intent(MainActivity.this, ActivityA.class);
        intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
        startActivity(intent);
    }


    public void doB(View v) {

        Intent intent = new Intent(MainActivity.this, ActivityB.class);
        intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
        activityResultLauncher.launch(intent);
    }

    public void handleResult(ActivityResult result) {

        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            String text = data.getStringExtra("USER_TEXT_IDENTIFIER");

            if (text == null) {
                Toast.makeText(this, "Null text value returned", Toast.LENGTH_SHORT).show();
                return;
            }
            if (text.isEmpty())
                Toast.makeText(this, "Empty text returned", Toast.LENGTH_SHORT).show();

            userText.setText(text);

            Log.d(TAG, "onActivityResult: User Text: " + text);
        } else {
            Log.d(TAG, "onActivityResult: result Code: " + result.getResultCode());
        }
    }

}

package com.riddhidamani.civil_advocacy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    private static final String API_URL = "https://developers.google.com/civic-information/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_about);

    }

    public void clickAPIURL(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(API_URL));
        // Check if there is an app that can handle https intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No App Found");
            builder.setMessage("No Application found that handles ACTION_VIEW (https) intents");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "The Back button was pressed - Going to Main Activity!", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }

}
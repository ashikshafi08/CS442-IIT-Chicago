package com.example.testvoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private TextView officialNameTxt, partyNameTxt;
    private ImageView officicalPhotoView, partyLogoPhotoAct;
    public Civic tempCivicObj;
    public String imageUrl;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        // Getting the intent
        Intent intent = getIntent();

        // Initialize the views
        officialNameTxt = findViewById(R.id.officialNamePhotoAct);
        partyNameTxt = findViewById(R.id.partyNamePhotoAct);
        officicalPhotoView = findViewById(R.id.officialImagePhotoAct);
        partyLogoPhotoAct = findViewById(R.id.partyLogoPhotoActId);
        constraintLayout = findViewById(R.id.photoActConstraintId);

        if (intent.hasExtra("PHOTO_OBJ_OFFICIAL")) {
            tempCivicObj = (Civic) intent.getSerializableExtra("PHOTO_OBJ_OFFICIAL");

            if (tempCivicObj != null) {
                loadData(tempCivicObj);
            }


        }
    }


    private void loadData(Civic civic) {

        String officialName = civic.getOfficialName();
        String partyName = civic.getParty();

        officialNameTxt.setText(officialName);
        partyNameTxt.setText(partyName);

        // Setting up the logo for the party and background color changes accordingly
        String partyNameTxt = civic.getParty();
        if (partyNameTxt != null) {
            // Changing the background accordingly
            if (partyNameTxt.contains("Democratic")) {
                constraintLayout.setBackgroundColor(Color.BLUE);
                partyLogoPhotoAct.setImageResource(R.drawable.dem_logo);

            } else if (partyNameTxt.contains("Republican")) {
                constraintLayout.setBackgroundColor(Color.RED);
                partyLogoPhotoAct.setImageResource(R.drawable.rep_logo);
            } else {
                constraintLayout.setBackgroundColor(Color.BLACK);
                partyLogoPhotoAct.setVisibility(ImageView.GONE);
            }

            Picasso picasso = Picasso.get();

            picasso.load(civic.getPhotoUrl()).error(R.drawable.brokenimage).placeholder(R.drawable.missing).into(officicalPhotoView);

        }


    }
}
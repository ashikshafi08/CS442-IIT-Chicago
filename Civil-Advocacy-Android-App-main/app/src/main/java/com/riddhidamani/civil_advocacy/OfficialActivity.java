package com.riddhidamani.civil_advocacy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Office tempOfficeObj;
    private Picasso picasso;
    private ConstraintLayout constraintLayout;
    private ImageView officialPhotoView;
    private ImageView partyIcon;
    private TextView officialLocation;
    private TextView officialOffice;
    private TextView officialName;
    private TextView officialParty;
    private TextView addressLine;
    private TextView phoneLine;
    private TextView websiteLine;
    private TextView emailLine;
    private String imageURL = "";
    private String fbID;
    private String tID;
    private String yID;
    private static int IMAGE_OPEN_REQUEST = 1;
    private static final String DEM_URL = "https://democrats.org";
    private static final String REP_URL = "https://www.gop.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        officialLocation = findViewById(R.id.officialLocation);
        officialPhotoView = findViewById(R.id.officialPhotoView);
        partyIcon = findViewById(R.id.partyIcon);
        officialOffice = findViewById(R.id.officialOffice);
        officialName = findViewById(R.id.officialName);
        officialParty = findViewById(R.id.officialParty);
        addressLine = findViewById(R.id.addressLine);
        phoneLine = findViewById(R.id.phoneLine);
        websiteLine = findViewById(R.id.websiteLine);
        emailLine = findViewById(R.id.emailLine);
        constraintLayout = findViewById(R.id.photoConstraintLayout);
        picasso = Picasso.get();

        picasso.setLoggingEnabled(true);
        Intent intent = getIntent();

        if(intent.hasExtra("OPEN_OFFICIAL")) {
            tempOfficeObj = (Office) intent.getSerializableExtra("OPEN_OFFICIAL");
            if(tempOfficeObj != null) {
                if(tempOfficeObj.getPhotoURL() != null){
                    imageURL = tempOfficeObj.getPhotoURL();
                }
//                // Check this once:
                else{
                    imageURL = "https://images-assets.nasa.gov/image/6900952/does_not_exist.jpg";
                }
            }
        }

        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            String location = intent.getStringExtra(Intent.EXTRA_TEXT);
            officialLocation.setText(location);
        }

        loadOfficialData();
        loadRemoteImage();

        // Usage of Linkify to link all the text views
        Linkify.addLinks(addressLine, Linkify.MAP_ADDRESSES);
        Linkify.addLinks(websiteLine, Linkify.ALL);
        Linkify.addLinks(phoneLine, Linkify.ALL);
        Linkify.addLinks(emailLine, Linkify.ALL);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
    }

    private void handleResult(ActivityResult result) {
        // do nothing
    }

    private void loadOfficialData(){

        officialOffice.setText(tempOfficeObj.getOffice());
        officialName.setText(tempOfficeObj.getName());

        String strParty = tempOfficeObj.getParty();
        if(strParty != null){
            officialParty.setText("(" + strParty + ")");

            if(strParty.contains("Democratic")) {

                constraintLayout.setBackgroundColor(Color.BLUE);
                partyIcon.setImageResource(R.drawable.dem_logo);
            }
            else if (strParty.contains("Republican")) {

                constraintLayout.setBackgroundColor(Color.RED);
                partyIcon.setImageResource(R.drawable.rep_logo);
            }
            else {
                constraintLayout.setBackgroundColor(Color.BLACK);
                partyIcon.setVisibility(ImageView.GONE);
            }
        }

        if(tempOfficeObj.getAddress() != null) {
            addressLine.setText(tempOfficeObj.getAddress());
        }
        else {
            TextView addressTitle = findViewById(R.id.addressTitle);
            addressTitle.setVisibility(TextView.GONE);
            addressLine.setVisibility(TextView.GONE);
        }

        if(tempOfficeObj.getPhoneNum() != null) {
            phoneLine.setText(tempOfficeObj.getPhoneNum());
        }
        else {
            TextView phoneTitle = findViewById(R.id.phoneTitle);
            phoneTitle.setVisibility(TextView.GONE);
            phoneLine.setVisibility(TextView.GONE);
        }

        if(tempOfficeObj.getWebURL() != null) {
            websiteLine.setText(tempOfficeObj.getWebURL());
        }
        else
            {
            TextView webTitle = findViewById(R.id.websiteTitle);
            webTitle.setVisibility(TextView.GONE);
            websiteLine.setVisibility(TextView.GONE);
        }

        String emailStr = tempOfficeObj.getEmailID();
        if(tempOfficeObj.getEmailID() != null) {
            emailLine.setText(tempOfficeObj.getEmailID());
        }
        else {
            TextView emailTitle = findViewById(R.id.emailTitle);
            emailTitle.setVisibility(TextView.GONE);
            emailLine.setVisibility(TextView.GONE);
        }

        if(tempOfficeObj.getFbID() != null) {
            fbID = tempOfficeObj.getFbID();
        }
        else {
            ImageView fbIcon = findViewById(R.id.facebook);
            fbIcon.setVisibility(ImageView.GONE);
        }

        if(tempOfficeObj.getTwitterID() != null){
            tID = tempOfficeObj.getTwitterID();
        }
        else {
            ImageView tIcon = findViewById(R.id.twitter);
            tIcon.setVisibility(ImageView.GONE);
        }

        if(tempOfficeObj.getYoutubeID() != null){
            yID = tempOfficeObj.getYoutubeID();
        }
        else {
            ImageView yIcon = findViewById(R.id.youtube);
            yIcon.setVisibility(ImageView.GONE);
        }
    }

    private void loadRemoteImage() {
        if (!doNetworkCheck()) {
            officialPhotoView.setImageResource(R.drawable.brokenimage);
            return;
        }
        Picasso.get().load(imageURL).error(R.drawable.missing).placeholder(R.drawable.placeholder).into(officialPhotoView,
                new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: Size:" + ((BitmapDrawable) officialPhotoView.getDrawable()).getBitmap().getByteCount());
                    }
                    @Override
                    public void onError(Exception exception) {
                        Log.d(TAG, "onError: Inside loadImages() function:" + exception.getMessage());
                    }
                });
    }

    // Performing Network Check
    private boolean doNetworkCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Toast.makeText(this, "Cannot access Connectivity Manager", Toast.LENGTH_SHORT).show();
            return false;
        }
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork == null) ? false : activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void fbClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + fbID;
        Intent intent;
        String urlToUse;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana",0);
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if(versionCode >= 3002850) {
                // newer version of Facebook app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }
            else
                { // older
                urlToUse = "fb://page/" + fbID;
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        }
        catch (Exception e){
            // if no app, open through web browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }
        startActivity(intent);
    }

    public void twitterClicked(View v) {
        String twitterAppUrl = "twitter://user?screen_name=" + tID;
        String twitterWebUrl = "https://twitter.com/" + tID;
        Intent intent;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        }
        catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }
        startActivity(intent);
    }

    public void youtubeClicked(View view) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + yID));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + yID)));
        }
    }

    public void officialPhotoClicked(View view) {
        if(tempOfficeObj.getPhotoURL() != null) {
            Intent intent = new Intent(this, PhotoActivity.class);
            String location = officialLocation.getText().toString();
            String party = officialParty.getText().toString();
            intent.putExtra("OFFICE", tempOfficeObj);
            intent.putExtra("LOCATION", location);
            activityResultLauncher.launch(intent);
        }
    }

    public void partyIconClicked(View view) {
        Intent intent = null;
        if(tempOfficeObj.getParty().contains("Democratic")){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DEM_URL));
        }
        else if(tempOfficeObj.getParty().contains("Republican")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(REP_URL));
        }
        else{
            Log.d(TAG, "clickPartyIcon: ERROR!");
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "The Back button was pressed - Going to Main Activity!", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }
}
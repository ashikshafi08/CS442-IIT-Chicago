package com.example.testvoteapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class OfficialDetailsActivity extends AppCompatActivity {

    private static final String TAG = "OfficialDetailsActivity" ;
    private Civic intentOfficeObj;
    private ConstraintLayout constraintLayout;
    public String location , facebookTxt , twitterTxt , youtubeTxt;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public String addressTxt, phoneNumberTxt , emailTxt , websiteUrlTxt;

    private static final String demoUrl = "https://democrats.org";
    private static final String repuUrl = "https://www.gop.com";

    // Initializing the Text Views
    private TextView officialName , partyName , address , officeName, phoneNumber , emailAddress, websiteUrl;
    private ImageView officialImageView , partyLogoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_details);

        initializeBinders();

        Intent data = getIntent();

        if(data.hasExtra("LOCATION")){
            location = data.getStringExtra("LOCATION");
            TextView locationAct = findViewById(R.id.locationActId);
            locationAct.setText(location.toUpperCase());
        }

        if(data.hasExtra("OFFICIAL_OBJ")){
            intentOfficeObj = (Civic) data.getSerializableExtra("OFFICIAL_OBJ");
            if(intentOfficeObj != null){
                setDataOfficalActivity(intentOfficeObj);
            }else{
                Log.d(TAG , "Its null");
            }
        }


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);




    }

    private void handleResult(ActivityResult activityResult) {
    }

    @SuppressLint("CutPasteId")
    private void initializeBinders(){
         officialName = findViewById(R.id.officialNameActId);
         officeName = findViewById(R.id.OfficeNameActId);
         partyName = findViewById(R.id.partyActId);

         address = findViewById(R.id.addressActId);
         phoneNumber = findViewById(R.id.phoneNumberActId);
         emailAddress = findViewById(R.id.emailActId);
         websiteUrl = findViewById(R.id.websiteActId);

         partyLogoView = findViewById(R.id.partyLogoId);
         constraintLayout = findViewById(R.id.officialDetailsConstraintId);

         officialImageView = findViewById(R.id.officialPhotoActId);

    }

    private void setDataOfficalActivity(Civic civic){

         Picasso picasso = Picasso.get();

         officeName.setText(civic.getOfficeName());
         officialName.setText(civic.getOfficialName());

        // Setting up the logo for the party and background color changes accordingly
         String partyNameTxt = civic.getParty();
        if (partyNameTxt != null){
            partyName.setText(String.format("( %s )", civic.getParty()));

            // Changing the background accordingly
            if (partyNameTxt.contains("Democratic")){
                constraintLayout.setBackgroundColor(Color.BLUE);
                partyLogoView.setImageResource(R.drawable.dem_logo);


            }else if (partyNameTxt.contains("Republican")){
                constraintLayout.setBackgroundColor(Color.RED);
                partyLogoView.setImageResource(R.drawable.rep_logo);

            }else{
                constraintLayout.setBackgroundColor(Color.BLACK);
                partyLogoView.setVisibility(ImageView.GONE);
            }
        }

        // ImageView
        String officialImageUrl = civic.getPhotoUrl();
        picasso.load(officialImageUrl).error(R.drawable.brokenimage).placeholder(R.drawable.missing).into(officialImageView);


        // Setting up other Text Views
        if (civic.getAddress() != null){
            addressTxt = civic.getAddress();
            address.setText(civic.getAddress());
        }else{
            TextView addressLabel = findViewById(R.id.addressLabelId);
            addressLabel.setVisibility(TextView.GONE);
            address.setVisibility(TextView.GONE);
        }

        if (civic.getEmailId() != null){
            emailTxt = civic.getEmailId();
            emailAddress.setText(civic.getEmailId());
        }else{
            TextView emailLabel = findViewById(R.id.emailLabelId);
            emailLabel.setVisibility(TextView.GONE);
            emailAddress.setVisibility(TextView.GONE);
        }

        if (civic.getPhoneNumber() != null){
            phoneNumberTxt = civic.getPhoneNumber();
            phoneNumber.setText(civic.getPhoneNumber());
        }else{
            TextView phoneNumberLabel = findViewById(R.id.phoneNumberLabelId);
            phoneNumberLabel.setVisibility(TextView.GONE);
            phoneNumber.setVisibility(TextView.GONE);

        }

        if (civic.getUrlString() != null){
            websiteUrlTxt = civic.getUrlString();
            websiteUrl.setText(civic.getUrlString());
        }else{
            TextView websiteUrlLabel = findViewById(R.id.websiteUrlLabelId);
            websiteUrlLabel.setVisibility(TextView.GONE);
            websiteUrl.setVisibility(TextView.GONE);
        }


        // Setting up the Social Media Link + ImageView
        if (civic.getFacebookId() != null){
            facebookTxt = civic.getFacebookId();
        }else{
            ImageView facebookIcon = findViewById(R.id.facebookLinkId);
            facebookIcon.setVisibility(ImageView.GONE);
        }

        if (civic.getTwitterId() != null){
            twitterTxt = civic.getTwitterId();
        }else{
            ImageView twitterIcon = findViewById(R.id.twitterLinkId);
            twitterIcon.setVisibility(ImageView.GONE);
        }

        if (civic.getYoutubeId() != null){
            youtubeTxt = civic.getYoutubeId();
        }else{
            ImageView youtubeIcon = findViewById(R.id.youtubeLinkId);
            youtubeIcon.setVisibility(ImageView.GONE);
        }










    }

    public void clickTwitter(View v) {
        String twitterAppUrl = "twitter://user?screen_name=" + twitterTxt;
        String twitterWebUrl = "https://twitter.com/" + twitterTxt;

        Intent intent;
        // Check if Twitter is installed, if not we'll use the browser
        if (isPackageInstalled("com.twitter.android")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (twitter/https) intents");
        }
    }

    public void clickFacebook(View v) {
        String facebookUrl = "https://www.facebook.com/" + facebookTxt;

        Intent intent;
        String urlToUse;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana",0);
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if(versionCode >= 3002850) {
                // newer version of Facebook app
                urlToUse = "fb://facewebmodal/f?href=" + facebookUrl;
            }
            else
            {
                urlToUse = "fb://page/" + facebookTxt;
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        }
        catch (Exception e){
            // if no app, open through web browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
        }
        startActivity(intent);

    }

    public boolean isPackageInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void onPhotoClicked(View view) {

        if(intentOfficeObj.getPhotoUrl() != null){
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra("LOCATION" , location);
            intent.putExtra("PHOTO_OBJ_OFFICIAL" , intentOfficeObj);
            activityResultLauncher.launch(intent);

        }
    }

    public void partyIconClicked(View view) {
        Intent intent = null;
        if(intentOfficeObj.getParty().contains("Democratic")){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(demoUrl));
        }
        else if(intentOfficeObj.getParty().contains("Republican")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repuUrl));
        }
        else{
            Log.d(TAG, "clickPartyIcon: ERROR!");
        }
        startActivity(intent);
    }

    public void youtubeClicked(View view) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youtubeTxt));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + youtubeTxt)));
        }
    }


    public void addressOnClick(View view) {
        Toast.makeText(this,  addressTxt, Toast.LENGTH_SHORT).show();
    }
}
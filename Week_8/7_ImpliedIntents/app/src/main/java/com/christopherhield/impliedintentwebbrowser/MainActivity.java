package com.christopherhield.impliedintentwebbrowser;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String msURL = "https://www.microsoft.com";

    // Don't forget INTERNET permission and the queries
    // entries in the manifest!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickMS(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(msURL));

        // Check if there is an app that can handle https intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (https) intents");
        }
    }

    public void clickEmail(View v) {
        String[] addresses = new String[]{"christopher.hield@gmail.com", "some.person@somemail.edu"};

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "This comes from EXTRA_SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT, "Email text body from EXTRA_TEXT...");

        // Check if there is an app that can handle mailto intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles SENDTO (mailto) intents");
        }
    }

    public void clickMap(View v) {
        String address = "Shedd Aquarium, 1200 S. Lake Shore Drive, Chicago, IL, 60605";

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        // Check if there is an app that can handle geo intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
        }
    }

    public void clickFacebook(View v) {
        // You need the FB user's id for the url
        String FACEBOOK_URL = "https://www.facebook.com/5953023255";

        Intent intent;

        // Check if FB is installed, if not we'll use the browser
        if (isPackageInstalled("com.facebook.katana")) {
            String urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }

        // Check if there is an app that can handle fb or https intents
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (fb/https) intents");
        }

    }

    public void clickCall(View v) {
        String number = "650-253-0000";

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_DIAL (tel) intents");
        }
    }

    public void clickTwitter(View v) {
        String user = "chicagotribune";
        String twitterAppUrl = "twitter://user?screen_name=" + user;
        String twitterWebUrl = "https://twitter.com/" + user;

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

    public void clickLinkedIn(View v) {
        String url = "https://www.linkedin.com/company/chicago-tribune-media-group/";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (twitter/https) intents");
        }
    }

    public void clickUber(View v) {

        String url = "uber://?action=setPickup" +
                "&pickup[latitude]=41.89293018346628&pickup[longitude]=-87.60308660084672" +
                "&dropoff[latitude]=41.879557214851395&dropoff[longitude]=-87.63820765257117";

        Intent intent;
        // Check if Uber is installed, if not we'll go to the playstore for app
        if (isPackageInstalled("com.ubercab")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab"));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (uber/https) intents");
        }

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

}

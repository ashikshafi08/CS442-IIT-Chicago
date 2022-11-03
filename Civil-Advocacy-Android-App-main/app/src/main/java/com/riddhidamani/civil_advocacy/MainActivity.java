package com.riddhidamani.civil_advocacy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient mFusedLocationClient;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final int LOCATION_REQUEST = 111;
    private static int OPEN_OFFICIAL_REQUEST = 0;
    private static String locationString = "Unspecified Location";
    private final List<Office> officialList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfficeAdapter officeAdapter;
    private String officialLocation;
    public String zipCode;
    private int position;
    private Office office;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        officeAdapter = new OfficeAdapter(officialList, this);
        recyclerView.setAdapter(officeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_items, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.about) {
            // Invoke About Activity
            openAboutActivity();
            return true;
        }
        else if(menuItem.getItemId() == R.id.search) {
            setManualLocation();
            return true;
        }
        else {
            Log.d(TAG, "onOptionsItemSelected: Unknown Item: " + menuItem.getTitle());
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // When search icon is clicked, Dialog Pops up!
    private void setManualLocation() {
        // Check for network Connection
        if(!doNetworkCheck()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Data cannot be accessed/loaded  without a network connection");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter a City, State, or a Zip Code:");
        final EditText manualLoc = new EditText(this);
        manualLoc.setGravity(Gravity.CENTER_HORIZONTAL);
        manualLoc.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(manualLoc);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setLocation(manualLoc.getText().toString());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nothing to cancel the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // When info icon is clicked, About Activity is opened
    private void openAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
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

    public void addOfficialData(Office newOfficeF) {
        if(newOfficeF == null)
        {
            Log.d(TAG, "addOffice: New Office is NULL");
        }
        officialList.add(newOfficeF);
        officeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        position = recyclerView.getChildLayoutPosition(view);
        office = officialList.get(position);
        openOfficialActivity(office);
    }

    private void openOfficialActivity(Office office) {
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("OPEN_OFFICIAL", office);
        intent.putExtra(Intent.EXTRA_TEXT, officialLocation);
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        //Toast.makeText(this,"Long Click", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void handleResult(ActivityResult result) {
        //Toast.makeText(this, "Coming from OfficialActivity Class", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                    return;
                }
            }
        }
        ((TextView) findViewById(R.id.location)).setText("NO Data For Location222");
    }


    private void determineLocation() {
        if(checkMyLocPermission()) {
            Log.d(TAG, "determineLocation: I have a permission!");
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                // Got last known location. In some situations this can be null.
                if (location != null) {
                    Log.d(TAG, "determineLocation: " + location.getLatitude() + ", " + location.getLatitude());
                    locationString = getPlace(location);
                    officialLocation = locationString;
                    ((TextView) findViewById(R.id.location)).setText(locationString);
                    DataDownloader dataDownloader = new DataDownloader(this, zipCode);
                    new Thread(dataDownloader).start();
                }
            }).addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }
        else {
            Log.d(TAG, "determineLocation: I DO NOT have a permission! ");
        }
    }

    public String getPlace(Location loc) {
        Log.d(TAG, "getPlace: ");
        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            Log.d(TAG, "getPlace: TRY");
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
            zipCode = postalCode;
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s, %s, %s",
                    city, state, postalCode));
        } catch (IOException e) {
            sb.append("No address found");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void setLocation(String input) {
        if (geocoder == null)
            geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            if(input.trim().isEmpty()) {
                Toast.makeText(this, "Please enter City, State or Zipcode to proceed!", Toast.LENGTH_LONG).show();
                return;
            }
            addresses = geocoder.getFromLocationName(input, 5);
            displayAddress(addresses);
        }
        catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        DataDownloader dataDownloader = new DataDownloader(this, input);
        officialList.clear();
        new Thread(dataDownloader).start();
    }

    private boolean checkMyLocPermission() {
        Log.d(TAG, "checkMyLocPermission: ");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkMyLocPermission: NOT YET GRANTED!");
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            return false;
        }
        return true;
    }

    private void displayAddress(List<Address> addresses) {
        if(addresses.size() == 0){
            ((TextView) findViewById(R.id.location)).setText("No Data For Location333");
            return;
        }
        Address one = addresses.get(0);
        // check if it's null or not
        String city = one.getLocality() == null ? "" : one.getLocality();
        String postalCode = one.getPostalCode() == null ? "" : one.getPostalCode();
        String state = one.getAdminArea() == null ? "" : one.getAdminArea();
        officialLocation = city + ", " + state + " " + postalCode;
        zipCode = postalCode;
        ((TextView) findViewById(R.id.location)).setText(city + ", " + state + " " + postalCode);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getResources().getString(R.string.bck_btn_bye_msg), Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }
}
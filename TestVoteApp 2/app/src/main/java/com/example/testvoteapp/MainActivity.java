package com.example.testvoteapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    public String location = "Chicago, IL";

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private HomeViewAdapter homeViewAdapter;
    private static RecyclerView homeRecyclerView;
    private static TextView addressBar;

    private Civic civicObj;
    private List<Civic> officialList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recycler View
        homeRecyclerView = findViewById(R.id.homeRecyclerViewId);
        addressBar = findViewById(R.id.addressId);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        homeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        DataDownloader.MainCivicDataDownloader(this, location);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);



    }

    private void handleResult(ActivityResult activityResult) {
    }


    public void updateData(Civic civicObj , ArrayList<Civic> civicArrayList){

        // Checking if null
        if (civicObj == null){
            Toast.makeText(this, "Can't find the class Civic.." , Toast.LENGTH_SHORT).show();
            return;
        }
//
//        for (int i = 0; i <civicArrayList.size(); i++) {
//            Civic newCivic = civicArrayList.get(i);
//
//            if (newCivic.getPhotoUrl() != null){
//                Log.d(TAG , newCivic.getPhotoUrl());
//            }else{
//                Log.d(TAG , "Error");
//            }
//        }



        HomeViewAdapter homeViewAdapter = new HomeViewAdapter(civicArrayList , this);

//        for (int i = 0; i <civicArrayList.size() ; i++) {
//           Civic dumObj = civicArrayList.get(i);
//           try{
//               Log.d(TAG , dumObj.getPhotoUrl());
//           } catch (Exception e) {
//               e.printStackTrace();
//               Log.d(TAG , "Return Null");
//           }
//        }

        homeRecyclerView.setAdapter(homeViewAdapter);
        // TextView civicAddressId = findViewById(R.id.addressId);
        // civicAddressId.setText(civicNewObj.getAddress());

        // Now lets unpack the whole
        officialList = civicArrayList;

    }

    @Override
    public void onClick(View v) {
        int position = homeRecyclerView.getChildLayoutPosition(v);
        civicObj = officialList.get(position);
        openOfficialsActivity(civicObj);

        // Toast.makeText(this, "The item is: " + position , Toast.LENGTH_SHORT).show();
    }

    private void openOfficialsActivity(Civic civic){
        Intent intent = new Intent(this, OfficialDetailsActivity.class);
        intent.putExtra("OFFICIAL_OBJ" , civic);
        intent.putExtra("LOCATION" , location);
        activityResultLauncher.launch(intent);

    }



    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.home_menu_layout , menu);
        return true;
    }

    private void doDownload(String location){
        DataDownloader.MainCivicDataDownloader(this , location);
    }

    private void openAboutActivity(){
        Intent intent = new Intent(this , AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.aboutInfoId){
            openAboutActivity();
            return true;
        }else if(item.getItemId() == R.id.locationChange){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Create an Edit Text
            final EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(editText);

            builder.setTitle("Enter the address");

            builder.setPositiveButton("OK", (dialog, id) -> {
                location = editText.getText().toString().trim();
                addressBar.setText(location.toUpperCase());
                doDownload(location);


            });

            builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
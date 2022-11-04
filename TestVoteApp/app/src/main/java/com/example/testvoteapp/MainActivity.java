package com.example.testvoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnLongClickListener{

    private static final String TAG = "MainActivity";
    public static String location = "Chicago, IL";

    private HomeViewAdapter homeViewAdapter;
    private static RecyclerView homeRecyclerView;

    private final List<Civic> officialList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recycler View
        homeRecyclerView = findViewById(R.id.homeRecyclerViewId);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DataDownloader.MainCivicDataDownloader(this, location);



    }


    public void updateData(Civic civicObj , ArrayList<Civic> civicArrayList){

        // Checking if null
        if (civicObj == null){
            Toast.makeText(this, "Can't find the class Civic.." , Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < civicArrayList.size(); i++) {

            // Get the single object
            Civic civicNewObj = civicArrayList.get(i);

           String officeName = civicNewObj.getOfficeName();


            officialList.add(civicNewObj);
            //homeViewAdapter.notifyDataSetChanged();




        }
        HomeViewAdapter homeViewAdapter = new HomeViewAdapter(officialList , this);

        homeRecyclerView.setAdapter(homeViewAdapter);
        Log.d(TAG , String.valueOf(officialList.size()));
        // TextView civicAddressId = findViewById(R.id.addressId);
        // civicAddressId.setText(civicNewObj.getAddress());

        // Now lets unpack the whole

    }

    @Override
    public void onClick(View v) {
        int position = homeRecyclerView.getChildLayoutPosition(v);
        Toast.makeText(this, "The item is: " + position , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
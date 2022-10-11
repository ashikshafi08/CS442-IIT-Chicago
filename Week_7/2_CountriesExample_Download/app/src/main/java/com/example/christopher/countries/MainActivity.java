package com.example.christopher.countries;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    // The above lines are important - them make this class a listener
    // for click and long click events in the ViewHolders (in the recycler


    // Don't forget you need:
    //      <uses-permission android:name="android.permission.INTERNET"/>

    private final List<Country> countryList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private CountryAdapter mAdapter; // Data to recyclerview adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);

        mAdapter = new CountryAdapter(countryList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load the data
        CountryLoaderVolley.getSourceData(this);
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Country c = countryList.get(pos);
        Intent intent = new Intent(MainActivity.this, CountryDetailActivity.class);
        intent.putExtra(Country.class.getName(), c);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        Toast.makeText(this, "Nothing Interesting Happens\nHere on Long Click", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void updateData(ArrayList<Country> cList) {
        countryList.addAll(cList);
        mAdapter.notifyItemRangeChanged(0, cList.size());
    }

    public void downloadFailed() {
        countryList.clear();
        mAdapter.notifyItemRangeChanged(0, countryList.size());
    }
}
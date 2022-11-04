package com.christopherhield.geography;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final ArrayList<Country> countryList = new ArrayList<>();
    private final HashMap<String, ArrayList<Country>> countryData = new HashMap<>();
    private Menu opt_menu;
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mConstraintLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<Country> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mConstraintLayout = findViewById(R.id.c_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Country c = countryList.get(position);
                    Intent intent = new Intent(MainActivity.this, CountryDetailActivity.class);
                    intent.putExtra(Country.class.getName(), c);
                    startActivity(intent);
                    mDrawerLayout.closeDrawer(mConstraintLayout);
                }
        );

        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        // Load the data
        if (countryData.isEmpty()) {
            CountryLoaderRunnable clr = new CountryLoaderRunnable(this);
            new Thread(clr).start();
        }


    }


    public void updateData(ArrayList<Country> listIn) {
        for (Country c : listIn) {
            if (!countryData.containsKey(c.getSubRegion())) {
                countryData.put(c.getSubRegion(), new ArrayList<>());
            }
            ArrayList<Country> clist = countryData.get(c.getSubRegion());
            if (clist != null) {
                clist.add(c);
            }
        }

        countryData.put("All", listIn);

        ArrayList<String> tempList = new ArrayList<>(countryData.keySet());
        Collections.sort(tempList);
        for (String s : tempList)
            opt_menu.add(s);


        countryList.addAll(listIn);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, countryList);
        mDrawerList.setAdapter(arrayAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void downloadFailed() {
        countryList.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    // You need the 2 below to make the drawer-toggle work properly:

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    // You need the below to open the drawer when the toggle is clicked
    // Same method is called when an options menu item is selected.

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        setTitle(item.getTitle());

        countryList.clear();
        ArrayList<Country> clist = countryData.get(item.getTitle().toString());
        if (clist != null) {
            countryList.addAll(clist);
        }

        arrayAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);

    }

    // You need this to set up the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }
}

package com.christopherhield.geography;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final HashMap<String, HashSet<String>> regionToSubRegion = new HashMap<>();
    private final HashMap<String, ArrayList<Country>> subRegionToCountries = new HashMap<>();
    private final ArrayList<Country> currentCountryList = new ArrayList<>();

    private final ArrayList<String> subRegionDisplayed = new ArrayList<>();
    private Menu opt_menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CountryAdapter countryAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);

        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    selectItem(position);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
        );

        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,            /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        countryAdapter = new CountryAdapter(this, currentCountryList);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(countryAdapter);

        new Thread(new CountryLoaderRunnable(this)).start();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void selectItem(int position) {

        viewPager.setBackground(null);

        String selectedSubRegion = subRegionDisplayed.get(position);
        currentCountryList.clear();

        ArrayList<Country> countries = subRegionToCountries.get(selectedSubRegion);

        if (countries == null) {
            Toast.makeText(this,
                    MessageFormat.format("No countries found for {0}", selectedSubRegion),
                    Toast.LENGTH_LONG).show();
            return;
        }
        currentCountryList.addAll(countries);
        countryAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);

        mDrawerLayout.closeDrawer(mDrawerList);

        setTitle(selectedSubRegion + " (" + countries.size() + ")");

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

        subRegionDisplayed.clear();
        HashSet<String> lst = regionToSubRegion.get(item.getTitle().toString());
        if (lst != null) {
            subRegionDisplayed.addAll(lst);
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

    public void downloadFailed() {
        Log.d(TAG, "downloadFailed: ");
    }

    public void updateData(ArrayList<Country> cList) {
        for (Country country : cList) {
            String region = country.getRegion();
            String subRegion = country.getSubRegion();

            if (!regionToSubRegion.containsKey(region))
                regionToSubRegion.put(region, new HashSet<>());
            Objects.requireNonNull(regionToSubRegion.get(region)).add(subRegion);

            if (!subRegionToCountries.containsKey(subRegion))
                subRegionToCountries.put(subRegion, new ArrayList<>());
            Objects.requireNonNull(subRegionToCountries.get(subRegion)).add(country);
        }

        ////

        ArrayList<String> tempList = new ArrayList<>(regionToSubRegion.keySet());

        Collections.sort(tempList);
        for (String s : tempList)
            opt_menu.add(s);


        arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, subRegionDisplayed);
        mDrawerList.setAdapter(arrayAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}

package com.example.dummyas5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout nDrawerLayout;
    private ListView nDrawerList;
    private List<String> categoryList = new ArrayList<>();

    private ActionBarDrawerToggle mDrawerToggle;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Sample data for the drawer layout
        items = new String[15];
        for (int i = 0; i < items.length; i++){
            categoryList.add("Drawer item #" + (i + 1));
        }


        nDrawerLayout = findViewById(R.id.drawer_layout);
        nDrawerList = findViewById(R.id.left_drawer);


        // the adapter
        nDrawerList.setAdapter(new ArrayAdapter<>(this ,
                R.layout.drawer_list_item , categoryList));

        nDrawerList.setOnItemClickListener(   // <== Important!
                (parent, view, position, id) -> selectItem(position)
        );

        // Binding the toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                nDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


    }

    private void selectItem(int position) {
        Toast.makeText(this, "You picked" + categoryList.get(position), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState(); // <== IMPORTANT
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Important!
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            Toast.makeText(this, "CLicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig); // <== IMPORTANT
    }
}
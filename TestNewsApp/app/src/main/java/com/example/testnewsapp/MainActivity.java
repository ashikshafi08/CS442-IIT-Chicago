package com.example.testnewsapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity: ";

    private static NewsSource newsSource;
    private Menu menu;
    List<NewsSource> categoryList = new ArrayList<>();
    private List<NewsSource> sourceObjList = new ArrayList<>();

    // Setting up the Drawer Layout
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView checkApiResponse;


        // Sample data for the drawer layout
        items = new String[15];
        for (int i = 0; i < items.length; i++)
            items[i] = "Drawer item #" + (i + 1);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);


        // the adapter
        mDrawerList.setAdapter(new ArrayAdapter<>(this ,
                R.layout.drawer_list_item , items));

        // Binding the toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }





//        NewsDataDownloader.MainNewsDataDownloader(this, "cnn");
//        getSomething();


    }

    public void sourceUpdateData(NewsSource newsSource , ArrayList<NewsSource> newsSourceArrayList){

        if (newsSource == null){
            Toast.makeText(this, "Can't find the class Civic.." , Toast.LENGTH_SHORT).show();
            return;
        }



        for (int i = 0; i < newsSourceArrayList.size() ; i++) {
            NewsSource newsSourceObj = newsSourceArrayList.get(i);
            //categoryList.add(newsSourceObj.getSourceCategory());
        }

        sourceObjList = new ArrayList<NewsSource>(newsSourceArrayList);


    }

    public void getSomething(){
        newsSource = sourceObjList.get(2);
        Log.d(TAG , newsSource.toString());

    }




    public void articleUpdateData(NewsArticle newsArticle ,  ArrayList<NewsArticle> newsArticleArrayList){

        if (newsArticle == null){
            Toast.makeText(this, "Can't find the class Civic.." , Toast.LENGTH_SHORT).show();
            return;
        }

//        Log.d(TAG , String.valueOf(newsSource.getMenuCategoryList()));
////
////        for (int i = 0; i < newsArticleArrayList.size() ; i++) {
////            NewsArticle newsArticleObj = newsArticleArrayList.get(i);
////        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.clear();

//        for (int i = 0; i < menuList.size() ; i++) {
//            menu.add(menuList.get(i));
//        }
        return true;
    }






}



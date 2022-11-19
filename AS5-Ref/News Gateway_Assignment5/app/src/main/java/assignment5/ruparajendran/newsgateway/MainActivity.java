package assignment5.ruparajendran.newsgateway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newsgateway.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Menu menu;
    RecyclerView recyclerView;
    NewsReceiver newsReceiver;
    public int pos=0;

    private ActionBarDrawerToggle mDrawerToggle;
    private HashMap<String, ArrayList<Sources>> sourcesData = new HashMap<>();
    private String[] items;

    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    static final String ARTICLE_LIST = "ARTICLE_LIST";
    static final String SOURCE = "SOURCE";
    List<Fragment> fragments;
    MyPageAdapter pageAdapter;
    ViewPager pager;
    String abc,cat,sid;

    ArrayList<Sources> sourceArrayList;
    ArrayList<Article> articleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceArrayList = new ArrayList<>();
        articleArrayList = new ArrayList<>();
        newsReceiver = new NewsReceiver();
        fragments = new ArrayList<>();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);


        Intent serviceIntent = new Intent(this, NewsService.class);
        startService(serviceIntent);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Sources temp = sourceArrayList.get(position);
                        Intent intent = new Intent(MainActivity.ACTION_MSG_TO_SERVICE);
                        intent.putExtra(SOURCE, temp);
                        sendBroadcast(intent);
                        setTitle(temp.getName());
                        abc = temp.getName();
                        cat = temp.getCategory();
                        sid = temp.getId();

                        pager.setVisibility(View.VISIBLE);
                        //pager.setBackgroundResource(R.color.);
                        //Snackbar.make(view,temp.getName() + " Selected", Snackbar.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );


        if(sourcesData.isEmpty()){
            SourcesRunnable sourcesRunnable = new SourcesRunnable(this);
            new Thread(sourcesRunnable).start();
        }

        if(networkCheck())
        {
            SourcesRunnable sourcesRunnable = new SourcesRunnable(this);
            new Thread(sourcesRunnable).start();

        }
        else
        {
        }

        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter1);
    }

    public void setupCategory(HashMap<String, ArrayList<Sources>> sourcesMapIn) {

        sourcesData.clear();

        try
        {
            menu.clear();
            sourcesData = sourcesMapIn;
            int i = 0;
            ArrayList<String> tempList = new ArrayList<>(sourcesData.keySet());

            Collections.sort(tempList);
            for(String category: tempList)
            {
                menu.add((category));
            }

            sourceArrayList.addAll(Objects.requireNonNull(sourcesData.get("all")));
            mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item,sourceArrayList));

            if (getSupportActionBar() != null)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "bp: setSources: Menu object did not inflate");
        }
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Check drawer first!
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        setTitle(item.getTitle().toString());

        sourceArrayList.clear();
        ArrayList<Sources> drawerTempList = sourcesData.get(item.getTitle().toString().toLowerCase());

        if(drawerTempList != null)
        {
            sourceArrayList.addAll(drawerTempList);
        }

        ((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();
        Toast.makeText(this, drawerTempList.size() + " Sources Loaded ", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {

        super.onSaveInstanceState(outState);
        outState.putInt("POS",pos);
        outState.putString("NAME", abc);
        outState.putString("CATEGORY", cat);
        outState.putString("ID", sid);

    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(newsReceiver);
        Intent i = new Intent(this,NewsService.class);
        stopService(i);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        pos = savedInstanceState.getInt("POS");
        abc = savedInstanceState.getString("NAME");
        cat = savedInstanceState.getString("CATEGORY");
        sid = savedInstanceState.getString("ID");
        Log.d(TAG, "onRestoreInstanceState: "+ pos);
        //setFragments(pos);
    }

    @Override
    protected void onResume()
    {
        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter1);
        super.onResume();
    }


    public boolean networkCheck()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null)
            return false;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void setFragments(int a)
    {
        if ( a == 0){
            for (int i = 0; i < pageAdapter.getCount(); i++) {
                pageAdapter.notifyChangeInPosition(i);
                pos = i;
                Log.d(TAG, "onRestoreInstanceState: "+ pos);
            }
        }


        fragments.clear();

        for (int i = 0; i < articleArrayList.size(); i++)
        {
            fragments.add(ArticleFragment.newInstance(articleArrayList.get(i), i+1, articleArrayList.size()));

        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    private class MyPageAdapter extends FragmentPagerAdapter
    {

        private long baseId = 0;

        MyPageAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getItemPosition(@NonNull Object object)
        {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            //pos = position;
            //position = pos;
            return fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return fragments.size();
        }

        @Override
        public long getItemId(int position)
        {
            return baseId + position;
        }

        void notifyChangeInPosition(int n)
        {
            baseId += getCount() + n;
        }


    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    class NewsReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            articleArrayList.clear();
            if (intent.hasExtra(ARTICLE_LIST))
            {
                articleArrayList = (ArrayList<Article>) intent.getSerializableExtra(ARTICLE_LIST);
                setFragments(0);
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
}
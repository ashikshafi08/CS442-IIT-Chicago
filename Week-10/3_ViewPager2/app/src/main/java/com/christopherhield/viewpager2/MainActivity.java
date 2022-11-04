package com.christopherhield.viewpager2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ZodiacAdapter zodiacAdapter;
    private final ArrayList<Zodiac> zodiacList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager2 = findViewById(R.id.view_pager);

        zodiacAdapter = new ZodiacAdapter(this, zodiacList);
        viewPager2.setAdapter(zodiacAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        new Thread(new ZodiacLoader(this)).start();
    }

    public void acceptResults(ArrayList<Zodiac> zodiacList) {
        if (zodiacList == null) {
            Toast.makeText(this, "Data loader failed", Toast.LENGTH_LONG).show();
        } else {
            this.zodiacList.addAll(zodiacList);
            zodiacAdapter.notifyItemRangeChanged(0, zodiacList.size());
        }
    }
}
package com.christopherhield.swiper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // You need on app gradle:
    //      implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

    private TextView textView;
    private SwipeRefreshLayout swiper;
    private int ctr = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        swiper = findViewById(R.id.swiper);

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                textView.setText(String.format(Locale.getDefault(), "Swipe Refresh Count: %d", ctr++));
                swiper.setRefreshing(false); // This stops the busy-circle
            }
        });
    }
}

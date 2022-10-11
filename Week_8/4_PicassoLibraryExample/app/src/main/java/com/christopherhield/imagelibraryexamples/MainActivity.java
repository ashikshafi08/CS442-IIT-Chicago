package com.christopherhield.imagelibraryexamples;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView imageView;
    private static final String urlString =
            "https://xcdn.britannica.com/33/194733-050-4CF75F31/Girl-with-a-Pearl-Earring-canvas-Johannes-1665.jpg";
    private long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        downloadImage();
    }

    public void doDownload(View v) {
        downloadImage();
    }

    private void downloadImage() {
        start = System.currentTimeMillis();
        Picasso.get().load(urlString)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        long time = System.currentTimeMillis() - start;
                        Log.d(TAG, "onSuccess: " + time);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError: " + e);
                    }
                });

    }
}
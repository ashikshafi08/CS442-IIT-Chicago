package com.christopherhield.volleyimagedownload;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView imageView;
    private long start;
    private static RequestQueue queue;
    private static final String urlString =
            "https://cdn.britannica.com/33/194733-050-4CF75F31/Girl-with-a-Pearl-Earring-canvas-Johannes-1665.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        queue = Volley.newRequestQueue(this);
        downloadImage();
    }

    private void downloadImage() {

        Response.Listener<Bitmap> listener = response -> {
            long time = System.currentTimeMillis() - start;
            Log.d(TAG, "downloadImage: " + time);
            imageView.setImageBitmap(response);
        };

        Response.ErrorListener error = error1 ->
                Log.d(TAG, "downloadImage: " + error1.getMessage());

        ImageRequest imageRequest =
                new ImageRequest(urlString, listener, 0, 0,
                        ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, error);
        start = System.currentTimeMillis();
        queue.add(imageRequest);
    }

    public void reloadImage(View view) {
        downloadImage();
    }
}
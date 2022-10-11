package com.example.volleyexample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    // Remember INTERNET permission in the manifest

    //https://developer.android.com/training/volley/simple#java
    //implementation 'com.android.volley:volley:1.2.1'
    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String iconUrl = "https://openweathermap.org/img/w/";

    //////////////////////////////////////////////////////////////////////////////////
    // Sign up to get your API Key at:  https://home.openweathermap.org/users/sign_up
    private static final String yourAPIKey = "6bfc226f3d6885de5b239a8c33047524";
    //
    //////////////////////////////////////////////////////////////////////////////////

    private final String TAG = getClass().getSimpleName();
    private RequestQueue queue;
    private long start;

    private EditText editText;
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.imageView);

        queue = Volley.newRequestQueue(this);

        String input = editText.getText().toString();
        doDownload(input);
    }

    public void getWeather(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        String input = editText.getText().toString();
        if (input.trim().isEmpty())
            return;

        doDownload(input);
    }

    private void doDownload(String input) {
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("q", input);
        buildURL.appendQueryParameter("appid", yourAPIKey);
        String urlToUse = buildURL.build().toString();

        start = System.currentTimeMillis();

        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            textView.setText(MessageFormat.format("Response: {0}", response.toString()));
                            JSONArray weather = response.getJSONArray("weather");
                            String icon = ((JSONObject) weather.get(0)).getString("icon");
                            runOnUiThread(() -> getIcon(icon));
                            setTitle(MessageFormat.format(
                                    "Duration: {0} ms", System.currentTimeMillis() - start));
                        } catch (Exception e) {
                            textView.setText(MessageFormat.format("Response: {0}", e.getMessage()));
                        }
                    }
                };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    textView.setText(MessageFormat.format("Error: {0}", jsonObject.toString()));
                    setTitle("Duration: " + (System.currentTimeMillis() - start));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void getIcon(String icon) {
        String imageURL = iconUrl + icon + ".png";

        Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setTitle(MessageFormat.format(
                        "Image Error: {0}", error.networkResponse.statusCode));
            }
        };

        ImageRequest imageRequest =
                new ImageRequest(imageURL, listener, 0, 0,
                        ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, error);
        queue.add(imageRequest);
    }
}
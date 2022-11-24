package com.example.testnewsapp;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceDataDownloader {

    private static final String TAG = "SourceDataDownloader";
    private RequestQueue requestQueue;
    private static NewsSource newsSource;
    private static final ArrayList<NewsSource> NewsSourceDataArrayList = new ArrayList<>();
    private MainActivity mainActivity;

    private static final String apiKey = "7fa404ac545e420081c647854f070196";




    public static void NewsSourceDownloader(MainActivity mainActivity){
        // Intialize the Volley
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        // Building the url
        String sourceApiUrl = "https://newsapi.org/v2/sources?apiKey=" + apiKey;

        Response.Listener<JSONObject> listener =
                response -> {
                    parseSourceJSON(response.toString());
                    mainActivity.sourceUpdateData(newsSource , NewsSourceDataArrayList);
                };

        Response.ErrorListener sourceError =
                error1 -> Toast.makeText(mainActivity, "Didn't get the API Response" , Toast.LENGTH_SHORT).show();


        JsonObjectRequest sourceJsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, sourceApiUrl.toString(),
                        null, listener, sourceError) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };

        queue.add(sourceJsonObjectRequest);

    }





    private static void parseSourceJSON(String s){
        try {
            List<String> categoryList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(s);
            // Get the source JSOn Array
            JSONArray sourceJSONArray = jsonObject.getJSONArray("sources");

            for (int i = 0; i < sourceJSONArray.length() ; i++) {
                newsSource = new NewsSource();

                JSONObject eachJSONObj = (JSONObject) sourceJSONArray.get(i);

                String sourceID = eachJSONObj.getString("id");
                newsSource.setSourceId(sourceID);

                String sourceName = eachJSONObj.getString("name");
                newsSource.setSourceName(sourceName);

                String sourceCategory = eachJSONObj.getString("category");
                newsSource.setSourceCategory(sourceCategory);


                NewsSourceDataArrayList.add(new NewsSource(newsSource.getSourceId() , newsSource.getSourceName(),
                        newsSource.getSourceCategory()));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}




package com.example.testnewsapp;

import android.util.Log;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NewsDataDownloader {

    private static final String TAG = "NewsDataDownloader";
    private RequestQueue requestQueue;
    private MainActivity mainActivity;

    private static NewsArticle newsArticle;
    private static NewsSource newsSource;

    private static final ArrayList<NewsSource> NewsSourceDataArrayList = new ArrayList<>();
    private static final ArrayList<NewsArticle> NewsArticleDataArrayList = new ArrayList<>();

    // TODO: Need two responses, one for the sources and another for the Articles
    private static final String apiKey = "7fa404ac545e420081c647854f070196";
    public String sourceName = "cnn";




    public static void MainNewsDataDownloader(MainActivity mainActivity , String sourceName){

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

        // Response for the Article URL
        String articleApiUrl = "https://newsapi.org/v2/top-headlines?sources=" +sourceName + "&apiKey=" + apiKey;

        Response.Listener<JSONObject> listener1 =
                response -> {
                    parseArticleJSON(response.toString());
                    mainActivity.articleUpdateData(newsArticle , NewsArticleDataArrayList);
                };

        Response.ErrorListener articleError =
                error1 -> Toast.makeText(mainActivity, "Didn't get the API Response" , Toast.LENGTH_SHORT).show();

        JsonObjectRequest articleJsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, articleApiUrl.toString(),
                        null, listener1, articleError) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };
        queue.add(articleJsonObjectRequest);


    }

    private static void parseArticleJSON(String s){

        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray articleJSONArray = jsonObject.getJSONArray("articles");

            for (int i = 0; i < articleJSONArray.length(); i++) {
                newsArticle = new NewsArticle();

                JSONObject jsonObject1 = (JSONObject) articleJSONArray.get(i);

                if (jsonObject1.has("author")){
                    String author = jsonObject1.getString("author");
                    newsArticle.setArticleAuthor(author);
                }

                if (jsonObject1.has("title")){
                    String title = jsonObject1.getString("title");
                    newsArticle.setArticleTitle(title);
                }

                if (jsonObject1.has("description")){
                    String description = jsonObject1.getString("description");
                    newsArticle.setArticleDescription(description);
                }

                if (jsonObject1.has("url")){
                    String url = jsonObject1.getString("url");
                    newsArticle.setArticleUrl(url);
                }

                if (jsonObject1.has("urlToImage")){
                    String imageUrl = jsonObject1.getString("urlToImage");
                    newsArticle.setArticleImageUrl(imageUrl);
                }

                if (jsonObject1.has("publishedAt")){
                    String datePublished = jsonObject1.getString("publishedAt");
                    newsArticle.setArticlePublishedTime(datePublished);
                }


                NewsArticleDataArrayList.add(new NewsArticle(newsArticle.getArticleAuthor() , newsArticle.getArticleTitle() ,
                        newsArticle.getArticleDescription() , newsArticle.getArticleUrl() , newsArticle.getArticleImageUrl() ,
                        newsArticle.getArticlePublishedTime()));

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

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

package assignment5.ruparajendran.newsgateway;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ArticleRunnable implements Runnable {

    private static final String TAG = "ArticleRunnable";
    private MainActivity mainActivity;
    private String id;
    private NewsService newsService;
    private static final String yourAPIKey = "31545f40a63649a89d4d3fdf4632f8d3";
    private static String DATA_URL = "https://newsapi.org/v2/top-headlines?sources=";
    private static String API_URL = "&language=en&apiKey=";

    ArticleRunnable(NewsService newsService, String id) {
        this.newsService = newsService; this.id = id;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(DATA_URL + id + API_URL + yourAPIKey);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "run: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent", "");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "run: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
            handleResults(null);
            return;
        }

        handleResults(sb.toString());

    }

    private void handleResults(String s) {

//        if (s == null) {
//            Log.d(TAG, "handleResults: Failure in data download");
//            mainActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //mainActivity.downloadFailed();
//                }
//            });
//            return;
//        }

        final ArrayList<Article> articleList = parseJSON(s);
        Log.d(TAG, "handleResults: " + articleList);
        newsService.setArticles(articleList);
//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (articleList != null)
//                    Toast.makeText(mainActivity, "Loaded " + articleList.size() + " countries.", Toast.LENGTH_LONG).show();
//                Log.d(TAG, "run: " + articleList.size());
//               // mainActivity.updateOfficialsData(articleList);
//            }
//        });
    }

    private ArrayList<Article> parseJSON(String s) {
        JSONArray jarticles=null;

        ArrayList<Article> articlesArrayList = new ArrayList<>();
        Log.d(TAG, "parseJSON list: " + articlesArrayList);

        try {
            JSONObject jObjMain = new JSONObject(s);

            try {
                jarticles = jObjMain.getJSONArray("articles");
                for (int j = 0; j < jarticles.length(); j++) {
                    String aauthor = "", atitle = "", adescription = "",aurl="",aurltoimage="",apublishedAt="";
                    JSONObject jarticle = null;

                    try {
                        jarticle = (JSONObject) jarticles.get(j);
                    } catch (Exception e) {

                    }
                    try {
                        aauthor = jarticle.getString("author");
                    } catch (Exception e) {

                    }
                    try {
                        atitle = jarticle.getString("title");
                    } catch (Exception e) {

                    }
                    try {
                        adescription = jarticle.getString("description");
                    } catch (Exception e) {

                    }
                    try {
                        aurl = jarticle.getString("url");
                    } catch (Exception e) {

                    }
                    try {
                        aurltoimage = jarticle.getString("urlToImage");
                    } catch (Exception e) {

                    }
                    try {
                        apublishedAt = jarticle.getString("publishedAt");
                    } catch (Exception e) {

                    }
                    articlesArrayList.add(new Article(aauthor,atitle,adescription,aurl,aurltoimage,apublishedAt));
                    Log.d(TAG, "parseJSON Listcountry: " + articlesArrayList.get(0).getUrltoImage());
                }
                return articlesArrayList;
            } catch (Exception e) { }
        }
        catch (Exception e){

        }
        return null;
    }
}




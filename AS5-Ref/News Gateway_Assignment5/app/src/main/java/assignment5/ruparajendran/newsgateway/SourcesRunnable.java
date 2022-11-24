package assignment5.ruparajendran.newsgateway;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SourcesRunnable implements Runnable {

    private static final String TAG = "SourcesRunnable";
    private MainActivity mainActivity;
    private static final String yourAPIKey = "7fa404ac545e420081c647854f070196";
    private static String DATA_URL = "https://newsapi.org/v2/sources?language=en&country=us&category=&apiKey=";

    SourcesRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(DATA_URL + yourAPIKey);
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

        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mainActivity.downloadFailed();
                }
            });
            return;
        }

        final HashMap<String, ArrayList<Sources>> sourceMapList = parseJSON(s);
        Log.d(TAG, "handleResults: " + sourceMapList);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sourceMapList != null)
                    Toast.makeText(mainActivity, "Loaded " + sourceMapList.size() + " countries.", Toast.LENGTH_LONG).show();
                Log.d(TAG, "run: " + sourceMapList.size());
                mainActivity.setupCategory(sourceMapList);
            }
        });
    }

    private HashMap<String, ArrayList<Sources>> parseJSON(String s) {

        JSONArray jsources=null;

        ArrayList<Sources> sourceArrayList = new ArrayList<>();
        Sources sourceobj;
        HashMap<String, ArrayList<Sources>> sourceMap = new HashMap<>();
        ArrayList<Sources> temp;
        Log.d(TAG, "parseJSON list: " + sourceArrayList);

        try {
            JSONObject jObjMain = new JSONObject(s);

            try {
                jsources = jObjMain.getJSONArray("sources");
                for (int j = 0; j < jsources.length(); j++) {
                    String sourceid = "", sourcename = "", sourcecategory = "";
                    JSONObject jsource = null;
                    sourceobj = new Sources();

                    try {
                        jsource = (JSONObject) jsources.get(j);

                    } catch (Exception e) {

                    }
                    try {
                        sourceid = jsource.getString("id");
                        sourceobj.setId(sourceid);
                    } catch (Exception e) {

                    }
                    try {
                        sourcename = jsource.getString("name");
                        sourceobj.setName(sourcename);
                    } catch (Exception e) {

                    }
                    try {
                        sourcecategory = jsource.getString("category");
                        sourceobj.setCategory(sourcecategory);
                    } catch (Exception e) {

                    }
                    sourceArrayList.add(sourceobj);


                    if(sourceMap.containsKey(sourceobj.getCategory().toLowerCase()))
                    {
                        temp = sourceMap.get(sourceobj.getCategory());
                        temp.add(sourceobj);
                        sourceMap.put(sourceobj.getCategory(),temp);
                    }
                    else
                    {
                        sourceMap.put(sourceobj.getCategory(),new ArrayList<Sources>());
                        temp = sourceMap.get(sourceobj.getCategory());
                        temp.add(sourceobj);
                        sourceMap.put(sourceobj.getCategory(),temp);
                    }

                }
                sourceMap.put("all", sourceArrayList);
                Log.d(TAG, "parseJSON Listcountry: " + sourceMap.get("all").get(0).getId() + " " + sourceMap.get("all").get(0).getName() + " " + sourceMap.get("all").get(0).getCategory());
                return sourceMap;
            } catch (Exception e) { }
        }
        catch (Exception e){

        }
        return null;
    }


}



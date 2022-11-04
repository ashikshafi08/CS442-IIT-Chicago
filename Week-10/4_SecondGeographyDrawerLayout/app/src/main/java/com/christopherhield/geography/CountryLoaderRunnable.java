package com.christopherhield.geography;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;

import com.caverock.androidsvg.SVG;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;


public class CountryLoaderRunnable implements Runnable {

    private static final String TAG = "CountryLoaderRunnable";
    private final MainActivity mainActivity;
    private static final String DATA_URL = "https://restcountries.com/v3.1/all";

    public CountryLoaderRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "run: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
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
            mainActivity.runOnUiThread(mainActivity::downloadFailed);
            return;
        }

        final ArrayList<Country> countryList = parseJSON(s);
        if (countryList == null) {
            mainActivity.runOnUiThread(mainActivity::downloadFailed);
            return;
        }

        mainActivity.runOnUiThread(
                () -> mainActivity.updateData(countryList));
    }

    private ArrayList<Country> parseJSON(String s) {

        String name;
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                JSONObject nameObj = jCountry.getJSONObject("name");
                name = nameObj.getString("common");

                String capital = "None";
                if (jCountry.has("capital")) {
                    JSONArray capArr = jCountry.getJSONArray("capital");
                    capital = capArr.getString(0);
                }

                Log.d(TAG, "parseJSON: " + name);
                String region = jCountry.getString("region");
                String subRegion = "Other";
                if (jCountry.has("subregion")) {
                    subRegion = jCountry.getString("subregion");
                }

                int population = jCountry.getInt("population");

                int area = jCountry.getInt("area");

                String citizen = "N/A";
                if (jCountry.has("demonyms")) {
                    JSONObject demonymObj = jCountry.getJSONObject("demonyms");
                    JSONObject engObj = demonymObj.getJSONObject("eng");
                    citizen = engObj.getString("m");
                }

                StringBuilder borders = new StringBuilder();
                if (!jCountry.has("borders")) {
                    borders.append("None");
                } else {
                    JSONArray jBorders = jCountry.getJSONArray("borders");
                    for (int j = 0; j < jBorders.length(); j++) {
                        borders.append(jBorders.get(j)).append(" ");
                    }
                }

                String flag = null;
                if (jCountry.has("flags")) {
                    JSONObject flagsObj = jCountry.getJSONObject("flags");
                    if (flagsObj.has("svg"))
                        flag = flagsObj.getString("svg");
                    else
                        Log.d(TAG, "parseJSON: ");
                } else {
                    Log.d(TAG, "parseJSON: ");
                }

                Drawable drawable = null;
                try {
                    InputStream input = new java.net.URL(flag).openStream();
                    SVG svg = SVG.getFromInputStream(input);
                    drawable = new PictureDrawable(svg.renderToPicture());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                countryList.add(
                        new Country(name, capital, population, region, subRegion,
                                area, citizen, borders.toString(), drawable));

            }
            Collections.sort(countryList);
            return countryList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

package com.christopherhield.viewpager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ZodiacLoader implements Runnable {

    private final MainActivity mainActivity;

    public ZodiacLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        try {
            InputStream is =
                    mainActivity.getResources().openRawResource(R.raw.zodiac_data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }

            ArrayList<Zodiac> zodiacList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String symbol = jsonObject.getString("symbol");
                String name = jsonObject.getString("name");
                String dates = jsonObject.getString("dates");

                Zodiac zodiac = new Zodiac(symbol, name, dates);
                zodiacList.add(zodiac);
            }

            mainActivity.runOnUiThread(() ->
                    mainActivity.acceptResults(zodiacList));
        } catch (Exception e) {
            mainActivity.runOnUiThread(() ->
                    mainActivity.acceptResults(null));
        }
    }
}

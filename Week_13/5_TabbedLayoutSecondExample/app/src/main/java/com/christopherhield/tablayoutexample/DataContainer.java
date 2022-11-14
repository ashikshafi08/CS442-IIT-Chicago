package com.christopherhield.tablayoutexample;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

class DataContainer {

    private static final String TAG = "DataContainer";
    private static final ArrayList<Person> people = new ArrayList<>();

    static ArrayList<Person> getPeopleList() {
        return people;
    }

    static void loadData(MainActivity context) {
        try {
            JSONArray jArr = loadJSONData(context);
            parseJSON(jArr);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        context.dataReady();

    }

    private static void parseJSON(JSONArray jArr) throws JSONException {
        for (int i = 0; i < jArr.length(); i++) {
            JSONObject jo = jArr.getJSONObject(i);
            String name = jo.getString("name");
            String dept = jo.getString("department");
            String loc = jo.getString("location");
            String phone = jo.getString("phone");

            String[] names = name.split(" ");

            Person p = new Person(names[0], names[1], dept, loc, phone);
            people.add(p);

        }

        Collections.sort(people);
        Log.d(TAG, "parseJSON: ");
    }

    private static JSONArray loadJSONData(Context context) throws IOException, JSONException {
        InputStream is = context.getResources().openRawResource(R.raw.people);

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            line = reader.readLine();
        }
        reader.close();

        return new JSONArray(sb.toString());

    }
}

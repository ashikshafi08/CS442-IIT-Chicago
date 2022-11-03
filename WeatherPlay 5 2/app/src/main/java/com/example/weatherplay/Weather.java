package com.example.weatherplay;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Weather implements Serializable {


    private final ArrayList<JSONObject> dayArrayList;
    private final String datetimeEpoch;
    private final String feelslike;
    private final String uvindex;
    private final String humidity;
    private final String icon;
    private final String visibility;
    private final String sunsetEpoch;
    private final String sunriseEpoch;
    private final String cloudcover;
    private final String conditions;
    private final String windir;
    private final String windspeed;
    private final String windgust;
    private final String temp;
    private final String tzoffset;


    Weather(String datetimeEpoch, String feelslike,
            String uvindex, String humidity, String icon, String visibility, String sunsetEpoch, String sunriseEpoch,
            String cloudcover, String conditions, String windir, String windspeed, String windgust, String temp,
            ArrayList<JSONObject> dayArrayList , String tzoffset) {

        this.datetimeEpoch = datetimeEpoch;
        this.feelslike = feelslike;
        this.uvindex = uvindex;
        this.humidity = humidity;
        this.icon = icon;
        this.visibility = visibility;
        this.sunsetEpoch = sunsetEpoch;
        this.sunriseEpoch = sunriseEpoch;
        this.cloudcover = cloudcover;
        this.conditions = conditions;
        this.windir = windir;
        this.windspeed = windspeed;
        this.windgust = windgust;
        this.temp = temp;
        this.dayArrayList = dayArrayList;
        this.tzoffset = tzoffset;
    }




    public String getDatetimeEpoch() {
        return datetimeEpoch;
    }

    public String getFeelslike() {
        return feelslike;
    }

    public ArrayList<JSONObject> getDayArrayList(){
        return dayArrayList;
    }

    public String getTzoffset(){return tzoffset;}

    public String getUvindex() {
        return uvindex;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getIcon() {
        return icon;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getSunsetEpoch() {
        return sunsetEpoch;
    }

    public String getSunriseEpoch() {
        return sunriseEpoch;
    }

    public String getCloudcover() {
        return cloudcover;
    }

    public String getConditions() {
        return conditions;
    }

    public String getWindir() {
        return windir;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public String getWindgust() {
        return windgust;
    }

    public String getTemp() {
        return temp;
    }

}







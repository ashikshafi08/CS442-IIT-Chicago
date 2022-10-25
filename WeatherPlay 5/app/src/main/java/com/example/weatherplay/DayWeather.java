package com.example.weatherplay;

import java.io.Serializable;

public class DayWeather implements Serializable {

    private final String tempMax;
    private final String tempMin;
    private final String datetimeEpoch;
    private final String description;
    private final String precipprob;
    private final String uvindex;
    private final String morningTemp;
    private final String noonTemp;
    private final String eveTemp;
    private final String nightTemp;
    private final String icon;


    public DayWeather(String tempMax, String tempMin,  String description, String datetimeEpoch,
                      String precipprob, String uvindex, String icon ,
                      String morningTemp , String noonTemp , String eveTemp , String nightTemp) {
        this.tempMax = tempMax;
        this.description = description;
        this.tempMin = tempMin;
        this.datetimeEpoch = datetimeEpoch;
        this.precipprob = precipprob;
        this.uvindex = uvindex;
        this.morningTemp = morningTemp;
        this.noonTemp = noonTemp;
        this.eveTemp = eveTemp;
        this.nightTemp = nightTemp;
        this.icon = icon;
    }


    public String getTempMax() {
        return tempMax;
    }

    public String getDescription() {
        return description;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getDatetimeEpoch() {
        return datetimeEpoch;
    }

    public String getPrecipprob() {
        return precipprob;
    }

    public String getUvindex() {
        return uvindex;
    }

    public String getIcon() {
        return icon;
    }

    public String getMorningTemp() {
        return morningTemp;
    }

    public String getNoonTemp() {
        return noonTemp;
    }

    public String getEveTemp() {
        return eveTemp;
    }

    public String getNightTemp(){
        return nightTemp;
    }
}

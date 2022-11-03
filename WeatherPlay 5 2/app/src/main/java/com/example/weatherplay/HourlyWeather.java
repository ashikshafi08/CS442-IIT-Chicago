package com.example.weatherplay;

import java.io.Serializable;

public class HourlyWeather implements Serializable {


    private final String day;
    private final String time;
    private final String icon;
    private final String temp;
    private final String conditions;

    public HourlyWeather(String day, String time,
                         String icon, String temp, String conditions) {
        this.day = day;
        this.time = time;
        this.icon = icon;
        this.temp = temp;
        this.conditions = conditions;
    }



    public String getDay(){return day;}

    public String getTime(){return time;}

    public String getIcon() {
        return icon;
    }

    public String getTemp() {
        return temp;
    }

    public String getConditions() {
        return conditions;
    }
}

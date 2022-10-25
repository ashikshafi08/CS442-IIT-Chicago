package com.example.weatherplay;

import java.io.Serializable;

public class HourlyWeather implements Serializable {

    private final String datetimeEpochString;
    private final String datetimeEpoch;
    private final String icon;
    private final String temp;
    private final String conditions;

    public HourlyWeather(String datetimeEpochString, String datetimeEpoch,
                         String icon, String temp, String conditions) {
        this.datetimeEpochString = datetimeEpochString;
        this.datetimeEpoch = datetimeEpoch;
        this.icon = icon;
        this.temp = temp;
        this.conditions = conditions;
    }

    public String getDatetimeEpochString() {
        return datetimeEpochString;
    }

    public String getDatetimeEpoch() {
        return datetimeEpoch;
    }

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

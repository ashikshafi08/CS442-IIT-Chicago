package com.christopherhield.act2;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

public class Person implements Serializable {

    private final String name;
    private double hourlyRate;

    Person(String name, double hourlyRate) {
        this.name = name;
        this.hourlyRate = hourlyRate;
    }

    void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    double getHourlyRate() {
        return hourlyRate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Person:\n" + name + ", " +
                String.format(Locale.getDefault(), "$%.2f", hourlyRate);
    }
}

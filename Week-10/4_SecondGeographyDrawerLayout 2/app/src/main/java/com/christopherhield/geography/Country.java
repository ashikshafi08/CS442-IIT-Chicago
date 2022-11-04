package com.christopherhield.geography;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Country
        implements Comparable<Country>, Serializable { // Needed to add as extra

    private final String name;
    private final String capital;
    private final int population;
    private final String region;
    private final String subRegion;
    private final int area;
    private final String citizen;
    private final String borders;
    private final transient Drawable drawable;

    Country(String name, String capital, int population, String region,
            String subRegion, int area, String citizen,
            String borders, Drawable draw) {
        this.name = name;
        this.capital = capital;
        this.population = population;
        this.region = region;
        if (subRegion.isEmpty())
            this.subRegion = "Unspecified";
        else
            this.subRegion = subRegion;
        this.area = area;
        this.citizen = citizen;
        this.borders = borders;
        this.drawable = draw;
    }

    String getBorders() {
        return borders;
    }

    int getArea() {
        return area;
    }

    String getCitizen() {
        return citizen;
    }

    String getName() {
        return name;
    }

    String getCapital() {
        return capital;
    }

    int getPopulation() {
        return population;
    }

    String getRegion() {
        return region;
    }

    String getSubRegion() {
        return subRegion;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    @NonNull
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Country country) {
        return name.compareTo(country.name);
    }

}
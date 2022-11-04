package com.christopherhield.viewpager2;

public class Zodiac {

    private final String symbol;
    private final String name;
    private final String dates;

    public Zodiac(String symbol, String name, String dates) {
        this.symbol = symbol;
        this.name = name;
        this.dates = dates;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getDates() {
        return dates;
    }
}

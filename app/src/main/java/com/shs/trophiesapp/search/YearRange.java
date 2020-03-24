package com.shs.trophiesapp.search;

public class YearRange {

    private int yearFrom;
    private int yearTo;

    public YearRange(int yearFrom, int yearTo) {
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public int getYearTo() {
        return yearTo;
    }
}

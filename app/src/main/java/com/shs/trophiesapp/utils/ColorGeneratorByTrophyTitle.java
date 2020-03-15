package com.shs.trophiesapp.utils;

import android.graphics.Color;

public class ColorGeneratorByTrophyTitle {

    int colors[] = Constants.colors;

    private static ColorGeneratorByTrophyTitle single_instance = null;

    // private constructor restricted to this class itself
    private ColorGeneratorByTrophyTitle() {
    }

    // static method to create instance of Singleton class
    public static ColorGeneratorByTrophyTitle getInstance() {
        if (single_instance == null)
            single_instance = new ColorGeneratorByTrophyTitle();

        return single_instance;
    }


    public int getColorForTrophyTitle(String trophyTitle) {
        // TODO
        return colors[0];
    }
}


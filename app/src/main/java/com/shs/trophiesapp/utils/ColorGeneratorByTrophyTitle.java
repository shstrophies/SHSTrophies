package com.shs.trophiesapp.utils;

import android.graphics.Color;

public class ColorGeneratorByTrophyTitle {

    int colors[] = Constants.colors;
    int nextColorIndex = 0;

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
        // TODO: check if the trophyTitle is in the hashmap, if it is, get it and return the color for it,
        //       if it isn't then add it to the map with the next color
        nextColorIndex++;
        nextColorIndex = nextColorIndex % colors.length;
        return colors[nextColorIndex];

    }
}


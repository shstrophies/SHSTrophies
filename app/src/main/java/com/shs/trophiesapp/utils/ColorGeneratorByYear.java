package com.shs.trophiesapp.utils;

import android.graphics.Color;

import java.util.HashMap;

public class ColorGeneratorByYear {

    int colors[] = Constants.colors;
    int nextColorIndex = 0;


    private static ColorGeneratorByYear single_instance = null;

    // private constructor restricted to this class itself
    private ColorGeneratorByYear() {}

    // static method to create instance of Singleton class
    public static ColorGeneratorByYear getInstance()
    {
        if (single_instance == null)
            single_instance = new ColorGeneratorByYear();

        return single_instance;
    }


    public int getColorForYear(int year) {
        // TODO
        nextColorIndex++;
        nextColorIndex = nextColorIndex % colors.length;
        return colors[nextColorIndex];
    }
}

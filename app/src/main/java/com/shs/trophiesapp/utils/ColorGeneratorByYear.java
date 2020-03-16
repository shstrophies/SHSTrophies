package com.shs.trophiesapp.utils;

import android.graphics.Color;

import java.util.HashMap;

public class ColorGeneratorByYear {

    int colors[] = Constants.colors;
    int nextColorIndex = 0;

    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

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
        // TODO: check if the trophyTitle is in the hashmap, if it is, get it and return the color for it,
        //       if it isn't then add it to the map with the next color

        // At the beginning of method, already on next color

        // if year not in hashmap, make new key for it and increment color generator
        if( !map.containsKey(year) ){

            nextColorIndex++;
            nextColorIndex = nextColorIndex % colors.length;

            map.put(year, colors[nextColorIndex]);
        }

        // if it is in hashmap, dont do anything key-wise and return the color of the key

        return map.get(year);


    }
}

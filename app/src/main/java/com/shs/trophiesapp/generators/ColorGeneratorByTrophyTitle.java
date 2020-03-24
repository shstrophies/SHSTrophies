package com.shs.trophiesapp.generators;

import com.shs.trophiesapp.utils.Constants;

import java.util.HashMap;

public class ColorGeneratorByTrophyTitle {

    int colors[] = Constants.colors;
    int nextColorIndex = 0;

    private static ColorGeneratorByTrophyTitle single_instance = null;

    HashMap<String, Integer> map = new HashMap<String, Integer>();

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
        // check if the trophyTitle is in the hashmap, if it is, get it and return the color for it,
        //       if it isn't then add it to the map with the next color

        // At the beginning of method, already on next color

        // if trophyTitle not in hashmap, make new key for it and increment color generator
        if( !map.containsKey(trophyTitle) ){

            nextColorIndex++;
            nextColorIndex = nextColorIndex % colors.length;

            map.put(trophyTitle, colors[nextColorIndex]);
        }

        // if it is in hashmap, dont do anything key-wise and return the color of the key

        return map.get(trophyTitle);





    }
}


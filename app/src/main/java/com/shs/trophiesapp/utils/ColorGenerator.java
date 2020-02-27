package com.shs.trophiesapp.utils;


import java.util.Random;

public class ColorGenerator {
    int colors[];
    int nextColorIndex = 0;

    //Random r = new Random();

    public ColorGenerator(int colors[]) {
        if (colors != null) {
            this.colors = colors;
        }
    }

    public ColorGenerator getNextColor() {

        /*
        if(nextColorIndex != colors.length-1) {
            nextColorIndex++;
        }else{
            nextColorIndex = 0;
        }
        */

        nextColorIndex++;
        nextColorIndex = nextColorIndex % colors.length;


        return this;
    }

    public int getColor() {
        return this.colors[nextColorIndex];
    }
}

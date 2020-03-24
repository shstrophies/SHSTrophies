package com.shs.trophiesapp.generators;



public class ColorGenerator {
    int colors[];
    int nextColorIndex = 0;

    public ColorGenerator(int colors[]) {
        if (colors != null) {
            this.colors = colors;
        }
    }

    public ColorGenerator getNextColor() {
        nextColorIndex++;
        nextColorIndex = nextColorIndex % colors.length;
        return this;
    }

    public int getColor() {
        return this.colors[nextColorIndex];
    }
}

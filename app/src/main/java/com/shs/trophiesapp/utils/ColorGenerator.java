package com.shs.trophiesapp.utils;

public class ColorGenerator {
    int colors[];
    int nextColorIndex = 0;

    public ColorGenerator(int colors[]) {
        if (colors != null) {
            this.colors = colors;
        }
    }

    public ColorGenerator getNextColor() {
        // TODO
        return this;
    }

    public int getColor() {
        return this.colors[nextColorIndex];
    }
}

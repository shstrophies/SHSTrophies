package com.shs.trophiesapp.utils;

public class ColorGenerator {
    int colors[] = {android.graphics.Color.YELLOW, android.graphics.Color.BLUE, android.graphics.Color.RED};
    int nextColorIndex = 0;

    public ColorGenerator(int colors[]) {
        if (colors != null) {
            this.colors = colors;
        }
    }

    public ColorGenerator() {
    }

    public ColorGenerator getNextColor() {
        // TODO
        return this;
    }

    public int getColor() {
        return this.colors[nextColorIndex];
    }
}

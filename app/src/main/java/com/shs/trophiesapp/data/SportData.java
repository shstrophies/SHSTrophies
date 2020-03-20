package com.shs.trophiesapp.data;

public class SportData {
    public String name;
    public String imageUrl;

    public SportData(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

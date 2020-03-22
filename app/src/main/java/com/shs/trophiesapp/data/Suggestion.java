package com.shs.trophiesapp.data;

import java.io.Serializable;

public class Suggestion implements Serializable {
    String title;
    String subtitle;

    public Suggestion(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}

package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tutorial {
    private final long id;
    private final String title;
    private final String language;
    private final String format;
    private final double avgRating;
    private ArrayList<Tag> tags = null;
    private final LocalDateTime creationDate;
    private final String descr;
    private final String url;

    public Tutorial(long id, String title, String language, String format, double avgRating, LocalDateTime creationDate, String descr, String url) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.format = format;
        this.avgRating = avgRating;
        this.creationDate = creationDate;
        this.descr = descr;
        this.url = url;
    }


    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public String getUrl() {
        return url;
    }

    public String getLanguage() {
        return language;
    }

    public String getFormat() {
        return format;
    }

    public double getRating() {
        return avgRating;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getDescr() {
        return descr;
    }
}

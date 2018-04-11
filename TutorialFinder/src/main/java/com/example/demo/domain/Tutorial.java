package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tutorial {
    private final long id;
    private final String title;
    private final Language language;
    private final Format format;
    private final Rating avgRating;
    private ArrayList<Tag> tags = null;
    private final LocalDateTime creationDate;
    private final String descr;
    private final String url;

    public Tutorial(long id, String title, Language language, Format format, Rating avgRating, LocalDateTime creationDate, String descr, String url) {
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

    public Language getLanguage() {
        return language;
    }

    public Format getFormat() {
        return format;
    }

    public Rating getRating() {
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

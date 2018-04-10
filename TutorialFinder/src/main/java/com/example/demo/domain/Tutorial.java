package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tutorial {
    private final long id;
    private final Language language;
    private final Format format;
    private final Rating rating;
    private final ArrayList<Tag> tags;
    private final LocalDateTime createdDate;
    private final String description;

    public Tutorial(long id, Language language, Format format, Rating rating, ArrayList<Tag> tags, LocalDateTime createdDate, String description) {
        this.id = id;
        this.language = language;
        this.format = format;
        this.rating = rating;
        this.tags = tags;
        this.createdDate = createdDate;
        this.description = description;
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
        return rating;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getDescription() {
        return description;
    }
}

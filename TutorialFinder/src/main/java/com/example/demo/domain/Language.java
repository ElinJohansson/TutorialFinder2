package com.example.demo.domain;

public class Language {
    private final long id;
    private final String name;

    public Language(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

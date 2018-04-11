package com.example.demo.domain;

public class Language {
    private long id;
    private final String name;

    public Language(String name, int id) {
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

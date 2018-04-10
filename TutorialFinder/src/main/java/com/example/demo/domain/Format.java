package com.example.demo.domain;

public class Format {
    private final long id;
    private final String name;

    public Format(long id, String name) {
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

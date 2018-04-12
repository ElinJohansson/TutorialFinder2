package com.example.demo.domain;

public class Format {
    private  long id;
    private final String name;

    public Format(String name) {

        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

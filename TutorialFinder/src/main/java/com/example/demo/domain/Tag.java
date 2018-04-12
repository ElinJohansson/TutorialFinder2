package com.example.demo.domain;

public class Tag {
    private  long id;
    private final String name;

    public Tag( String name) {

        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}   

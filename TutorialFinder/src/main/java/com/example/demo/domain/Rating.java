package com.example.demo.domain;

public class Rating {
    private final long id;
    private final int value;

    public Rating(long id, int value) {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }
}

package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

//Represents json object
public class TutorialResource {
    private long id;
    private String title;
    private String language;
    private String format;
    private double avgRating;
    private ArrayList<Tag> tags = null;
    private LocalDateTime creationDate;
    private String descr;
    private String url;

}

package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface Repository {
    void createTutorial(String title, String descr, String language, String format, String url);

    int createNewLanguage(String name);

    void createNewTag(String name);

    int createNewFormat(String name);

    void addTags(List<String> tags, String title);
}

package com.example.demo.repository;

import com.example.demo.domain.Format;
import com.example.demo.domain.Language;
import com.example.demo.domain.Tutorial;

import java.util.List;

public interface Repository {
    void createTutorial(String title, String descr, String language, String format, String url);

    int createNewLanguage(String name);

    int createNewTag(String name);

    int createNewFormat(String name);

    void addTagsToTutorial(List<String> tags, String title);

    List<Language> getLanguages();

    List<Tutorial> getTutorials();


    List<Tutorial> getTutorialsByLanguage(List<String> languages, List<String> formats);

    List<Format> getFormats();
}




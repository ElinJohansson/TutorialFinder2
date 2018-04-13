package com.example.demo.repository;

import com.example.demo.domain.Format;
import com.example.demo.domain.Language;
import com.example.demo.domain.Tag;
import com.example.demo.domain.Tutorial;

import java.util.List;

public interface Repository {
    void createTutorial(String title, String descr, String language, String format, String url);

    int getFormatId(String format);

    int createNewLanguage(String name);

    int createNewTag(String name);

    int createNewFormat(String name);

    void addTagsToTutorial(List<String> tags, String title);

    List<Language> getLanguages();

    void addRatingToTutorial(String title, int rating);

    List<Tutorial> getTutorialsByLanguage(List<String> languages, List<String> formats, List<String> tags);

    List<Format> getFormats();

    List<Tutorial> getToplist();

    List<String> getTags();
}




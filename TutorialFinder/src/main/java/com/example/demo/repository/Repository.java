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

    int getFormatId(String format);
    int getLanguageId(String name);
    List<Language> getLanguages();
    List<Tutorial> getTutorialsByLanguage(List<String> languages, List<String> formats, List<String> tags);
    List<Format> getFormats();
    List<Tutorial> getToplist();
    List<String> getTags();

    void addTagsToTutorial(List<String> tags, String title);
    void addRatingToTutorial(String title, int rating);
}




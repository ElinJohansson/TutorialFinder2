package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface Repository {
    void createTutorial(String title, String descr, LocalDateTime creationDate, String language, String format, String url, List<String> tags);
}

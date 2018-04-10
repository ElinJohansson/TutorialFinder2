package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TutorialRepository implements Repository {

    @Autowired
    private DataSource dataSource;

}

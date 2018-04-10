package com.example.demo.controller;

import com.example.demo.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TutorialController {

    @Autowired
    private Repository repository;
}

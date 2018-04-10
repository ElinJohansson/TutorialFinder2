package com.example.demo.controller;

import com.example.demo.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class TutorialController {

    @Autowired
    private Repository repository;

    @GetMapping("/admin")
    public ModelAndView getAdminPage(){
        return new ModelAndView("admin");
    }

    @PostMapping("/addTutorial")
    public String getInfoFromAddTutorialForm(@RequestParam String title, @RequestParam String url, @RequestParam String language,
                                             @RequestParam String format, @RequestParam String descr,
                                             @RequestParam String tag) {
        //To get multiple tags from tag-string
        List<String> tags = Arrays.asList(tag.trim().split(" +"));
        repository.createTutorial(title, descr, language, format, url);
        repository.addTagsToTutorial(tags,title);
        return "redirect:/admin";
    }

    @PostMapping("/addTags")
    public String getInfoFromAddTutorialForm(@RequestParam String title, @RequestParam String tag) {
        //To get multiple tags from tag-string
        List<String> tags = Arrays.asList(tag.trim().split(" +"));
        repository.addTagsToTutorial(tags,title);
        return "redirect:/admin";
    }

}

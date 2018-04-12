package com.example.demo.controller;


import com.example.demo.domain.Format;
import com.example.demo.domain.Language;
import com.example.demo.domain.Tag;
import com.example.demo.domain.Tutorial;
import com.example.demo.repository.Repository;
import com.example.demo.repository.TutorialRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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

//    @GetMapping("/FilterTabell")              todo
//    public ModelAndView getFilterTabellPage(){
//        List <Language> languages = repository.getLanguages();
//        return new ModelAndView("Filtertabell").addObject("languages", languages);
//
//    }
    @GetMapping("/index")
    public ModelAndView getTutorialsByFilter(){
        List <Tutorial> tutorials = repository.getTutorials();
        List <Language> languages = repository.getLanguages();

        List <Format> formats = repository.getFormats();
        return new ModelAndView("index")
                .addObject("tutorials", tutorials)
                .addObject("languages", languages)
                .addObject("formats", formats);
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


    @PostMapping("/addRating")
    public String getInfoFromAddTutorialForm(@RequestParam String title, @RequestParam int rating) {
        repository.addRatingToTutorial(title, rating);
        return "redirect:/admin";
    }

    //Getmapping för ajaxanropet
    @GetMapping("/filterOnLanguage")
    public @ResponseBody List<Tutorial> getFilterOnLanguage(@RequestParam String language, @RequestParam String format){
        System.out.println("ajax request successful");
        if(language == null){
            throw new TutorialRepositoryException("No language was checked");
        }

        List<String> languages = null;
        if(language != null && language.trim().length() > 0)
            languages = new ArrayList<String>(Arrays.asList(language.split(",")));
        List<String> formats = null;
        if(format != null && format.trim().length() > 0)
            formats = new ArrayList<String>(Arrays.asList(format.split(",")));

        List<Tutorial> tutorials = repository.getTutorialsByLanguage(languages, formats);
        return tutorials;
    }

}

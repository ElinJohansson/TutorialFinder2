package com.example.demo.controller;

import com.example.demo.domain.Format;
import com.example.demo.domain.Language;
import com.example.demo.domain.Tutorial;
import com.example.demo.repository.Repository;
import com.example.demo.repository.TutorialRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TutorialController {

    @Autowired
    private Repository repository;

    @GetMapping("/admin")
    public ModelAndView getAdminPage() {
        return new ModelAndView("admin");
    }

    @GetMapping("/index")
    public ModelAndView getTutorialsByFilter(HttpSession session) {
        session.setAttribute("votedTitles", "");
        List<Language> languages = repository.getLanguages();
        List<Tutorial> topList = repository.getToplist();
        List<Format> formats = repository.getFormats();
        List<String> tags = repository.getTags();

        return new ModelAndView("index")
                .addObject("languages", languages)
                .addObject("formats", formats)
                .addObject("topList", topList)
                .addObject("tags", tags);
    }

    @PostMapping("/addTutorial")
    public String getInfoFromAddTutorialForm(@RequestParam String title, @RequestParam String url, @RequestParam String language,
                                             @RequestParam String format, @RequestParam String descr,
                                             @RequestParam String tag) {
        //To get multiple tags from tag-string
        List<String> tags = Arrays.asList(tag.trim().split(" +"));
        repository.createTutorial(title, descr, language, format, url);
        repository.addTagsToTutorial(tags, title);
        return "redirect:/admin";
    }

    @PostMapping("/addTags")
    public String getInfoFromAddTutorialForm(@RequestParam String title, @RequestParam String tag) {
        List<String> tags = Arrays.asList(tag.trim().split(" +"));
        repository.addTagsToTutorial(tags, title);
        return "redirect:/admin";
    }


    @PostMapping("/adminRating")
    public String getInfoFromAddTutorialForm(@RequestParam String title, @RequestParam int rating) {
        repository.addRatingToTutorial(title, rating);
        return "redirect:/admin";
    }

    //Getmapping för ajaxanropet
    @GetMapping("/filterOnLanguage")
    public @ResponseBody
    List<Tutorial> getFilterOnLanguage(@RequestParam String language, @RequestParam String format, @RequestParam String tag) {
        if (language == null) {
            throw new TutorialRepositoryException("No language was checked");
        }

        List<String> languages = null;
        if (language != null && language.trim().length() > 0)
            languages = new ArrayList<String>(Arrays.asList(language.split(",")));
        List<String> formats = null;
        if (format != null && format.trim().length() > 0)
            formats = new ArrayList<String>(Arrays.asList(format.split(",")));
        List<String> tags = null;
        if (tag != null && tag.trim().length() > 0)
            tags = new ArrayList<String>(Arrays.asList(tag.split(",")));

        List<Tutorial> tutorials = repository.getTutorialsByLanguage(languages, formats, tags);
        return tutorials;
    }

    //Postmapping för ajax-anrop
    @PostMapping("/addRating")
    public @ResponseBody
    String postRating(@RequestParam String rating, @RequestParam String title, HttpSession session) {
        String votedTitles = (String) session.getAttribute("votedTitles");
        if (votedTitles.indexOf(title) < 0) {
            session.setAttribute("votedTitles", session.getAttribute("votedTitles") + "~" + title);
            int rate = Integer.parseInt(rating);
            repository.addRatingToTutorial(title,rate);
        }
        return title;
    }
}

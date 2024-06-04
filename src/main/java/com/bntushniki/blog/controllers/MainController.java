package com.bntushniki.blog.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class handling the main page functionality.
 * @author Daniil
 */
@Controller
@RequestMapping("/main")
@Slf4j
public class MainController {

    /**
     * Displays the main page.
     * @param model The model to add attributes for rendering.
     * @return The view name for the main page.
     */
    @GetMapping
    public String home(Model model) {
        model.addAttribute("title", "Main page");
        log.info("Displayed the main page");
        return "home";
    }
}
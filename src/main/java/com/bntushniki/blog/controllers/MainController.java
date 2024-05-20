package com.bntushniki.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainController {

    @GetMapping
    public String home(Model model) {
        model.addAttribute("title", "Main page");
        return "home.html";
    }
}

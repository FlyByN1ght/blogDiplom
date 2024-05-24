package com.bntushniki.blog.security.controller;

import com.bntushniki.blog.security.service.UserSecurityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserSecurityService userSecurityService;

    @Autowired
    public LoginController(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    @GetMapping
    public String showLoginForm() {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam("loginOrEmail") String loginOrEmail,
                        @RequestParam("password") String password) {
        if (!userSecurityService.login(loginOrEmail, password)) {
            return "redirect:/login";
        }
        return "redirect:/main";
    }
}

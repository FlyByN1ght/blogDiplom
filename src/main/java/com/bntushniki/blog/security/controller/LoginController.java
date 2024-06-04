package com.bntushniki.blog.security.controller;

import com.bntushniki.blog.security.service.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user login.
 * @author Daniil
 */
@Controller
@RequestMapping("/login")
@Slf4j
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

    /**
     * Handles user login.
     *
     * @param loginOrEmail the user login or email
     * @param password     the user password
     * @return the redirection based on login success or failure
     */
    @PostMapping
    public String login(@RequestParam("loginOrEmail") String loginOrEmail,
                        @RequestParam("password") String password) {
        if (!userSecurityService.loginAndPassword(loginOrEmail, password)) {
            log.error("Failed login attempt for user: {}", loginOrEmail);
            return "redirect:/login";
        }
        log.info("User logged in successfully: {}", loginOrEmail);
        return "redirect:/main";
    }
}

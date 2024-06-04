package com.bntushniki.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class handling user logout functionality.
 * @author Daniil
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

    /**
     * the user out and redirects to the login page with a logout parameter.
     * @return A redirect to the login page with a logout parameter.
     */
    @GetMapping
    public String logout() {
        return "redirect:/login?logout";
    }
}

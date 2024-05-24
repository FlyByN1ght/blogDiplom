package com.bntushniki.blog.security.controller;

import com.bntushniki.blog.model.UserFaculty;
import com.bntushniki.blog.security.model.dto.RegistrationDto;
import com.bntushniki.blog.security.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserSecurityService userSecurityService;

    @Autowired
    public RegistrationController(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("faculties", UserFaculty.values());
        return "registration";
    }

    @PostMapping
    public String registerUser(@RequestParam("userLogin") String userLogin,
                               @RequestParam("userPassword") String userPassword,
                               @RequestParam("email") String email,
                               @RequestParam("lastName") String lastName,
                               @RequestParam("firstName") String firstName,
                               @RequestParam("faculty") UserFaculty faculty
                               ) {

        if (userSecurityService.registerUser(userLogin, userPassword, email, lastName, firstName, faculty)){
            return "redirect:/registration/success";
        } else {
            return "redirect:/registration/error";
        }
    }

    @GetMapping("/success")
    public String registrationSuccess() {
        return "successRegistration";
    }

    @GetMapping("/error")
    public String registrationError() {
        return "errorRegistration";
    }
}

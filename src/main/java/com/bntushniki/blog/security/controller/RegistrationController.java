package com.bntushniki.blog.security.controller;

import com.bntushniki.blog.model.UserFaculty;
import com.bntushniki.blog.security.model.UserRole;
import com.bntushniki.blog.security.service.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user registration.
 * @author Daniil
 */
@Controller
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {

    private final UserSecurityService userSecurityService;

    @Autowired
    public RegistrationController(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    /**
     * Displays the registration form.
     *
     * @param model the model
     * @return the registration form view
     */
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("faculties", UserFaculty.values());
        return "registration";
    }

    /**
     * Registers a new user.
     *
     * @param userLogin    the user login
     * @param userPassword the user password
     * @param email        the email
     * @param lastName     the last name
     * @param firstName    the first name
     * @param faculty      the faculty
     * @return the registration result view
     */
    @PostMapping
    public String registerUser(@RequestParam("userLogin") String userLogin,
                               @RequestParam("userPassword") String userPassword,
                               @RequestParam("email") String email,
                               @RequestParam("lastName") String lastName,
                               @RequestParam("firstName") String firstName,
                               @RequestParam("faculty") UserFaculty faculty) {

        if (userSecurityService.registerUser(userLogin, userPassword, email, lastName, firstName, faculty, UserRole.STUDENT)){
            log.info("User registered successfully: {}", userLogin);
            return "successRegistration";
        } else {
            log.error("User registration failed: {}", userLogin);
            return "errorRegistration";
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

    @GetMapping("/teacher/success")
    public String registrationForTeacherSuccess() {
        return "registrationForTeacherSuccess";
    }

    @GetMapping("/teacher/error")
    public String registrationForTeacherError() {
        return "registrationForTeacherError";
    }
}

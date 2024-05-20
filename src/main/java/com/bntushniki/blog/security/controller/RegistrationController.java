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
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUserLogin(userLogin);
        registrationDto.setUserPassword(userPassword);
        registrationDto.setEmail(email);
        registrationDto.setLastName(lastName);
        registrationDto.setFirstName(firstName);
        registrationDto.setFaculty(faculty);

        if(userSecurityService.userExistsByEmail(registrationDto.getEmail()) ||
                userSecurityService.userExistsByLogin(registrationDto.getUserLogin()) ||
                !userSecurityService.registerUser(registrationDto)) {
            return "redirect:/errorRegistration";
        } else {
            return "redirect:/registration/success";
        }
    }

    @GetMapping("/registration/success")
    public String registrationSuccess() {
        return "successRegistration";
    }

    @GetMapping("/registration/error")
    public String registrationError() {
        return "errorRegistration"; //
    }
}

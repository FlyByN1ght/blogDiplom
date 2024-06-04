package com.bntushniki.blog.controllers;

import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.service.UserSecurityService;
import com.bntushniki.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserSecurityService userSecurityService;
    private final UserService userService;

    public AdminController(UserSecurityService userSecurityService, UserService userService) {
        this.userSecurityService = userSecurityService;
        this.userService = userService;
    }

    @GetMapping("/menu")
    public String adminMenu(Model model) {
        List<Users> users = userService.findAll();
        Map<Users, UserSecurity> usersWithSecurityDetails = userSecurityService.getUserMapWithSecurityDetails(users);
        model.addAttribute("usersWithSecurityDetails", usersWithSecurityDetails);
        return "adminMenu";
    }

    @PostMapping("/users/toggleRole/{id}")
    public String toggleUserRole(@PathVariable("id") Long id) {
        userSecurityService.toggleUserRole(id);
        return "redirect:/admin/menu";
    }
}

package com.bntushniki.blog.controllers;

import lombok.extern.slf4j.Slf4j;

import com.bntushniki.blog.model.Comment;
import com.bntushniki.blog.model.Post;
import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.service.UserSecurityService;
import com.bntushniki.blog.service.CommentService;
import com.bntushniki.blog.service.PostService;
import com.bntushniki.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Controller class handling administrative functions.
 * @author Daniil
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final UserSecurityService userSecurityService;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public AdminController(UserSecurityService userSecurityService, UserService userService, PostService postService,
                           CommentService commentService) {
        this.userSecurityService = userSecurityService;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    /**
     * Displays the admin menu with options to manage users.
     * @param model The model to add attributes for rendering.
     * @return The view name for the admin menu page.
     */
    @GetMapping("/menu")
    public String adminMenu(Model model) {
        List<Users> users = userService.findAll();
        Map<Users, UserSecurity> usersWithSecurityDetails = userSecurityService.getUserMapWithSecurityDetails(users);
        model.addAttribute("usersWithSecurityDetails", usersWithSecurityDetails);
        log.info("Displayed admin menu");
        return "adminMenu";
    }

    /**
     * Toggles the role of a user between student and admin.
     * @param id The ID of the user whose role is to be toggled.
     * @return Redirects to the admin menu page.
     */
    @PostMapping("/users/toggleRole/{id}")
    public String toggleUserRole(@PathVariable("id") Long id) {
        userSecurityService.toggleUserRoleStudentToAdmin(id);
        log.info("Toggled role for user with ID {}", id);
        return "redirect:/admin/menu";
    }

    /**
     * Deletes a user by ID.
     * @param id The ID of the user to be deleted.
     * @return Redirects to the admin menu page.
     */
    @PostMapping("/users/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userSecurityService.deleteUserById(id);
        log.info("Deleted user with ID {}", id);
        return "redirect:/admin/menu";
    }

    /**
     * Displays posts created by a specific user.
     * @param id The ID of the user whose posts are to be displayed.
     * @param model The model to add attributes for rendering.
     * @return The view name for the page displaying user posts.
     */
    @GetMapping("/users/posts/{id}")
    public String getUserPosts(@PathVariable("id") Long id, Model model) {
        List<Post> posts = postService.getAllPostsByUserId(id);
        model.addAttribute("posts", posts);
        log.info("Displayed posts for user with ID {}", id);
        return "userPost";
    }

    /**
     * Displays comments made by a specific user.
     * @param id The ID of the user whose comments are to be displayed.
     * @param model The model to add attributes for rendering.
     * @return The view name for the page displaying user comments.
     */
    @GetMapping("/users/comments/{id}")
    public String getUserComments(@PathVariable("id") Long id, Model model) {
        List<Comment> comments = commentService.getAllComment(id);
        model.addAttribute("comments", comments);
        log.info("Displayed comments for user with ID {}", id);
        return "userComment";
    }
}

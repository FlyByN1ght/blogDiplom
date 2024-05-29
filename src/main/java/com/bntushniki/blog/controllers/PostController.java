package com.bntushniki.blog.controllers;

import com.bntushniki.blog.model.Post;
import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.service.UserSecurityService;
import com.bntushniki.blog.service.PostService;
import com.bntushniki.blog.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserSecurityService userSecurityService;
    private final UserService userService;

    public PostController(PostService postService, UserSecurityService userSecurityService, UserService userService) {
        this.postService = postService;
        this.userSecurityService = userSecurityService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreatePostForm() {
        return "createPost";
    }

    @GetMapping("/success")
    public String successSavePost() {
        return "createPostSuccess";
    }

    @GetMapping("/error")
    public String errorSavePost() {
        return "createPostError";
    }

    @PostMapping("/create")
    public String createPost(@AuthenticationPrincipal User user, @RequestParam("content") String content,
                             @RequestParam("namePost") String namePost) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(user.getUsername());
        if (userSecurityOptional.isEmpty()) {
            return "userNotFound";
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        if(postService.createPost(userSecurity.getUserId(), content, namePost))
        {
            return "redirect:success";
        }
        return "redirect:error";
    }

    @GetMapping("/view/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        Post post = postService.findPostById(postId);
        if (post == null) {
            return "postNotFound";
        }
        model.addAttribute("post", post);
        return "viewPost";
    }

    @GetMapping("/{userLogin}")
    public String viewAllPost(@PathVariable String userLogin, Model model) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(userLogin);
        if (userSecurityOptional.isEmpty()) {
            return "userNotFound";
        }

        UserSecurity userSecurity = userSecurityOptional.get();

        Optional<Users> usersOptional = userService.getUserById(userSecurity.getUserId());

        if (usersOptional.isEmpty()) {
            return "userNotFound";
        }
        Users users = usersOptional.get();


        model.addAttribute("users", users);
        model.addAttribute("posts", postService.getAllPostsByUserId(users.getUserId()));
        return "viewAllPost";
    }
}

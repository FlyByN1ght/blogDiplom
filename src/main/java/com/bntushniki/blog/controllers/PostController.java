package com.bntushniki.blog.controllers;

import com.bntushniki.blog.model.Comment;
import com.bntushniki.blog.model.Post;
import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.service.UserSecurityService;
import com.bntushniki.blog.service.CommentService;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserSecurityService userSecurityService;
    private final UserService userService;
    private final CommentService commentService;

    public PostController(PostService postService, UserSecurityService userSecurityService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userSecurityService = userSecurityService;
        this.userService = userService;
        this.commentService = commentService;
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

        List<Comment> comments = commentService.findByPostId(postId);
        List<String> commentUserNames = new ArrayList<>();

        for (Comment comment : comments) {
            Optional<Users> userOptional = userService.getUserById(comment.getUserId());
            userOptional.ifPresent(user -> commentUserNames.add(user.getFirstName() + " " + user.getLastName()));
        }

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("commentUserNames", commentUserNames);
        return "viewPost";
    }

    @GetMapping("/{userLogin}")
    public String viewAllPost(@PathVariable String userLogin, Model model) {
        Optional<Users> usersOptional = getUserByLogin(userLogin);
        if (usersOptional.isEmpty()) {
            return "userNotFound";
        }
        Users users = usersOptional.get();

        model.addAttribute("users", users);
        model.addAttribute("posts", postService.getAllPostsByUserId(users.getUserId()));
        return "viewAllPost";
    }

    @PostMapping("/comment/add")
    public String addComment(@RequestParam Long postId, @AuthenticationPrincipal User user,
                             @RequestParam String content, @RequestParam(required = false) Long parentCommentId) {
        Optional<Users> usersOptional = getUserByLogin(user.getUsername());
        if (usersOptional.isEmpty()) {
            return "userNotFound";
        }
        Users users = usersOptional.get();
        Comment comment = new Comment();
        comment.setUserId(users.getUserId());
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setTimestamp(new Timestamp(System.currentTimeMillis()));
        commentService.saveComment(comment);
        return "redirect:/post/view/" + postId;
    }

    private Optional<Users> getUserByLogin(String userLogin) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(userLogin);
        if (userSecurityOptional.isEmpty()) {
            return Optional.empty();
        }
        UserSecurity userSecurity = userSecurityOptional.get();
        return userService.getUserById(userSecurity.getUserId());
    }
}

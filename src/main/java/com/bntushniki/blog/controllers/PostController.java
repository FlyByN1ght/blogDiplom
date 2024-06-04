package com.bntushniki.blog.controllers;

import com.bntushniki.blog.model.Comment;
import com.bntushniki.blog.model.Post;
import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.service.UserSecurityService;
import com.bntushniki.blog.service.CommentService;
import com.bntushniki.blog.service.PostService;
import com.bntushniki.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Controller class handling post-related functionality.
 * @author Daniil
 */
@Controller
@RequestMapping("/post")
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserSecurityService userSecurityService;
    private final UserService userService;
    private final CommentService commentService;

    public PostController(PostService postService, UserSecurityService userSecurityService,
                          UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userSecurityService = userSecurityService;
        this.userService = userService;
        this.commentService = commentService;
    }

    /**
     * Displays the form for creating a new post.
     * @return The view name for the create post form.
     */
    @GetMapping("/create")
    public String showCreatePostForm() {
        return "createPost";
    }

    /**
     * Displays the success message after saving a post.
     * @return The view name for the success message.
     */
    @GetMapping("/success")
    public String successSavePost() {
        return "createPostSuccess";
    }

    /**
     * Displays the error message after failing to save a post.
     * @return The view name for the error message.
     */
    @GetMapping("/error")
    public String errorSavePost() {
        return "createPostError";
    }

    /**
     * Handles the creation of a new post.
     * @param user The authenticated user.
     * @param content The content of the post.
     * @param namePost The name of the post.
     * @return A redirect to either success or error page based on the result of post creation.
     */
    @PostMapping("/create")
    public String createPost(@AuthenticationPrincipal User user, @RequestParam("content") String content,
                             @RequestParam("namePost") String namePost) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(user.getUsername());
        if (userSecurityOptional.isEmpty()) {
            log.error("User security not found for user: {}", user.getUsername());
            return "userNotFound";
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        if(postService.createPost(userSecurity.getUserId(), content, namePost)) {
            log.info("Post created successfully by user: {}", user.getUsername());
            return "redirect:success";
        }
        log.error("Failed to create post by user: {}", user.getUsername());
        return "redirect:error";
    }

    /**
     * Displays a single post with its comments.
     * @param postId The ID of the post to view.
     * @param model The model to add attributes for rendering.
     * @return The view name for the post and its comments.
     */
    @GetMapping("/view/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        Post post = postService.findPostById(postId);
        if (post == null) {
            log.error("Post not found with ID: {}", postId);
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

    /**
     * Displays all posts created by a specific user.
     * @param userLogin The login of the user whose posts are to be viewed.
     * @param model The model to add attributes for rendering.
     * @return The view name for all posts created by the user.
     */
    @GetMapping("/{userLogin}")
    public String viewAllPost(@PathVariable String userLogin, Model model) {
        Optional<Users> usersOptional = getUserByLogin(userLogin);
        if (usersOptional.isEmpty()) {
            log.error("User not found with login: {}", userLogin);
            return "userNotFound";
        }
        Users users = usersOptional.get();

        model.addAttribute("users", users);
        model.addAttribute("posts", postService.getAllPostsByUserId(users.getUserId()));
        return "viewAllPost";
    }

    /**
     * Adds a comment to a post.
     * @param postId The ID of the post to which the comment is added.
     * @param user The authenticated user.
     * @param content The content of the comment.
     * @param parentCommentId The ID of the parent comment if it's a reply to another comment.
     * @return A redirect to view the post after adding the comment.
     */
    @PostMapping("/comment/add")
    public String addComment(@RequestParam Long postId, @AuthenticationPrincipal User user,
                             @RequestParam String content, @RequestParam(required = false) Long parentCommentId) {
        Optional<Users> usersOptional = getUserByLogin(user.getUsername());
        if (usersOptional.isEmpty()) {
            log.error("User not found for adding comment: {}", user.getUsername());
            return "userNotFound";
        }
        Users users = usersOptional.get();
        Comment comment = new Comment();
        comment.setUserId(users.getUserId());
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setTimestamp(new Timestamp(System.currentTimeMillis()));
        commentService.saveComment(comment);
        log.info("Comment added successfully by user: {}", user.getUsername());
        return "redirect:/post/view/" + postId;
    }

    /**
     * Retrieves a user by login.
     * @param userLogin The login of the user.
     * @return An optional containing the user if found, otherwise empty.
     */
    private Optional<Users> getUserByLogin(String userLogin) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(userLogin);
        if (userSecurityOptional.isEmpty()) {
            log.error("User security not found with login: {}", userLogin);
            return Optional.empty();
        }
        UserSecurity userSecurity = userSecurityOptional.get();
        return userService.getUserById(userSecurity.getUserId());
    }
}
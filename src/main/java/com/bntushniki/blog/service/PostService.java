package com.bntushniki.blog.service;

import com.bntushniki.blog.model.Post;
import com.bntushniki.blog.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing posts.
 * @author Daniil
 */
@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Creates a new post.
     *
     * @param userId    ID of the user creating the post
     * @param content   content of the post
     * @param namePost  title of the post
     * @return true if the post is successfully created, false otherwise
     */
    public boolean createPost(Long userId, String content, String namePost) {
        if (content.isBlank() || namePost.isBlank()) {
            return false;
        }
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);
        post.setNamePost(namePost);
        post.setCreated(new Timestamp(System.currentTimeMillis()));
        postRepository.save(post);
        log.info("Created a new post with ID: {}", post.getPostId());
        return true;
    }

    /**
     * Retrieves all posts by user ID.
     *
     * @param userId the ID of the user
     * @return a list of posts
     */
    public List<Post> getAllPostsByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }

    /**
     * Finds a post by its ID.
     *
     * @param postId the ID of the post to find
     * @return the post, or null if not found
     */
    public Post findPostById(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElse(null);
    }
}

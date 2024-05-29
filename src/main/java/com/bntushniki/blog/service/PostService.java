package com.bntushniki.blog.service;

import com.bntushniki.blog.model.Post;
import com.bntushniki.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean createPost(Long userId, String content, String namePost) {
        if(content.isBlank() || namePost.isBlank()){
            return false;
        }
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);
        post.setNamePost(namePost);
        post.setCreated(new Timestamp(System.currentTimeMillis()));
        postRepository.save(post);
        return true;
    }

    public List<Post> getAllPostsByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }

    public Post findPostById(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElse(null);
    }
}

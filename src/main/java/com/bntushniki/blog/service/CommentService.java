package com.bntushniki.blog.service;

import com.bntushniki.blog.model.Comment;
import com.bntushniki.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
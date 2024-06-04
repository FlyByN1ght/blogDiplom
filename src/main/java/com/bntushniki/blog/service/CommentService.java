package com.bntushniki.blog.service;

import com.bntushniki.blog.model.Comment;
import com.bntushniki.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing comments.
 * @author Daniil
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Finds comments by post ID.
     *
     * @param postId the post ID
     * @return a list of comments
     */
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    /**
     * Saves a comment.
     *
     * @param comment the comment to save
     * @return the saved comment
     */
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * Retrieves all comments by user ID.
     *
     * @param id the user ID
     * @return a list of comments
     */
    public List<Comment> getAllComment(Long id) {
        return commentRepository.findAllByUserId(id);
    }
}

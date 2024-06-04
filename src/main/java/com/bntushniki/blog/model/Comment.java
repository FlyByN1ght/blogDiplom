package com.bntushniki.blog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Class entity for comment
 * @author Daniil
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
public class Comment {
    @Id
    @SequenceGenerator(name = "commentSeqGen", sequenceName = "comment_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "commentSeqGen")
    private Long commentId;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_comm", nullable = false)
    private Timestamp timestamp;
}
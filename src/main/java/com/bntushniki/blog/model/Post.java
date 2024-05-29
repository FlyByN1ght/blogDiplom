package com.bntushniki.blog.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @SequenceGenerator(name = "postSeqGen", sequenceName = "post_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "postSeqGen")
    private Long postId;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_name", nullable = false)
    private String namePost;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_post", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;
}
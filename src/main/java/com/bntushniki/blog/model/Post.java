package com.bntushniki.blog.model;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_post", nullable = false)
    private Timestamp timestamp;
}
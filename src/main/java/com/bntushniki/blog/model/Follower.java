package com.bntushniki.blog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "followers")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follower_id")
    private Long followerId;

    @Column(name = "follower_user_id", nullable = false)
    private Long followerUserId;

    @Column(name = "following_user_id", nullable = false)
    private Long followingUserId;
}
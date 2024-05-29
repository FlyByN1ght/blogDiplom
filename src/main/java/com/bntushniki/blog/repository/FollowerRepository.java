package com.bntushniki.blog.repository;

import com.bntushniki.blog.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    @Query("SELECT COUNT(f) FROM followers f WHERE f.followerUserId = :userId")
    long countFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM followers f WHERE f.followingUserId = :userId")
    long countFollowingByUserId(@Param("userId") Long userId);

    @Query("SELECT f.followingUserId FROM followers f WHERE f.followerUserId = :userId")
    List<Long> findFollowersByUserId(Long userId);

    @Query("SELECT f.followerUserId FROM followers f WHERE f.followingUserId = :userId")
    List<Long> findFollowingByUserId(Long userId);

    @Query(value = "SELECT COUNT(*) > 0 FROM followers WHERE follower_user_id = :followerUserId AND following_user_id = :followingUserId", nativeQuery = true)
    boolean existsByFollowerUserIdAndFollowingUserId(@Param("followerUserId") Long followerUserId, @Param("followingUserId") Long followingUserId);

    void deleteByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);
}

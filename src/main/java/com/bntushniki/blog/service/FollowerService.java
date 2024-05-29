package com.bntushniki.blog.service;

import com.bntushniki.blog.model.Follower;
import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.repository.FollowerRepository;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import com.bntushniki.blog.security.service.UserSecurityService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FollowerService {
    private final FollowerRepository followerRepository;
    private final UserRepository usersRepository;
    private final UserSecurityService userSecurityService;
    private final UserService userService;
    private final UserSecurityRepository userSecurityRepository;

    @Autowired
    public FollowerService(FollowerRepository followerRepository, UserSecurityService userSecurityService,
                           UserService userService, UserRepository usersRepository, UserSecurityRepository userSecurityRepository) {
        this.followerRepository = followerRepository;
        this.userSecurityService = userSecurityService;
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.userSecurityRepository = userSecurityRepository;
    }

    public long countFollowers(Long userId) {
        return followerRepository.countFollowersByUserId(userId);
    }

    public long countFollowing(Long userId) {
        return followerRepository.countFollowingByUserId(userId);
    }

    public boolean isFollow(Long followerUserId, Long followingUserId) {
        return followerRepository.existsByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId);
    }

    public List<Users> getUsersByType(String userLogin, String type) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(userLogin);
        if (userSecurityOptional.isEmpty()) {
            return Collections.emptyList();
        }
        UserSecurity userSecurity = userSecurityOptional.get();
        Optional<Users> usersOptional = userService.getUserById(userSecurity.getUserId());
        if (usersOptional.isEmpty()) {
            return Collections.emptyList();
        }
        Users users = usersOptional.get();

        List<Long> userIds = Collections.emptyList();
        if ("followers".equals(type)) {
            userIds = followerRepository.findFollowersByUserId(users.getUserId());
        } else if ("following".equals(type)) {
            userIds = followerRepository.findFollowingByUserId(users.getUserId());
        }
        return usersRepository.findAllById(userIds);
    }

    public Map<Users, String> getUserMapWithLogins(List<Users> usersList) {
        Map<Users, String> userMapWithLogins = new HashMap<>();
        for (Users user : usersList) {
            Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserId(user.getUserId());
            userSecurityOptional.ifPresent(userSecurity -> userMapWithLogins.put(user, userSecurity.getUserLogin()));
        }
        return userMapWithLogins;
    }

    public Map<Users, String> getFollowerMapWithLogins(String userLogin) {
        List<Users> followers = getUsersByType(userLogin, "followers");
        return getUserMapWithLogins(followers);
    }

    public Map<Users, String> getFollowingMapWithLogins(String userLogin) {
        List<Users> following = getUsersByType(userLogin, "following");
        return getUserMapWithLogins(following);
    }

    @Transactional
    public void followUser(Long followerUserId, Long followingUserId) {
        if (!isFollow(followerUserId, followingUserId)) {
            Follower userFollower = new Follower();
            userFollower.setFollowerUserId(followerUserId);
            userFollower.setFollowingUserId(followingUserId);
            followerRepository.save(userFollower);
        }
    }

    @Transactional
    public void unfollowUser(Long followerUserId, Long followingUserId) {
        if (isFollow(followerUserId, followingUserId)) {
            followerRepository.deleteByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId);
        }
    }
}


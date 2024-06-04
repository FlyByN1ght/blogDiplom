package com.bntushniki.blog.service;

import com.bntushniki.blog.model.Follower;
import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.repository.FollowerRepository;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.service.UserSecurityService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing followers and followings.
 * @author Daniil
 */
@Service
@Slf4j
public class FollowerService {

    private static final Logger logger = LoggerFactory.getLogger(FollowerService.class);

    private final FollowerRepository followerRepository;
    private final UserRepository usersRepository;
    private final UserSecurityService userSecurityService;
    private final UserService userService;

    @Autowired
    public FollowerService(FollowerRepository followerRepository, UserSecurityService userSecurityService,
                           UserService userService, UserRepository usersRepository) {
        this.followerRepository = followerRepository;
        this.userSecurityService = userSecurityService;
        this.userService = userService;
        this.usersRepository = usersRepository;
    }

    /**
     * Gets the count of followers for a user.
     *
     * @param userId the user ID
     * @return the count of followers
     */
    public long countFollowers(Long userId) {
        return followerRepository.countFollowersByUserId(userId);
    }

    /**
     * Gets the count of followings for a user.
     *
     * @param userId the user ID
     * @return the count of followings
     */
    public long countFollowing(Long userId) {
        return followerRepository.countFollowingByUserId(userId);
    }

    /**
     * Checks if a user is following another user.
     *
     * @param followerUserId  the follower user ID
     * @param followingUserId the following user ID
     * @return true if the follower user is following the following user, otherwise false
     */
    public boolean isFollow(Long followerUserId, Long followingUserId) {
        return followerRepository.existsByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId);
    }

    /**
     * Retrieves users based on the type (followers or following) for a given user.
     *
     * @param userLogin the login of the user
     * @param type      the type of users to retrieve (followers or following)
     * @return a list of users based on the type
     */
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

    /**
     * Retrieves a map of followers with their associated logins for a given user.
     *
     * @param userLogin the login of the user
     * @return a map of followers with their logins
     */
    public Map<Users, String> getFollowerMapWithLogins(String userLogin) {
        List<Users> followers = getUsersByType(userLogin, "followers");
        return userService.getUserMapWithLogins(followers);
    }

    /**
     * Retrieves a map of followings with their associated logins for a given user.
     *
     * @param userLogin the login of the user
     * @return a map of followings with their logins
     */
    public Map<Users, String> getFollowingMapWithLogins(String userLogin) {
        List<Users> following = getUsersByType(userLogin, "following");
        return userService.getUserMapWithLogins(following);
    }

    /**
     * Allows a user to follow another user.
     *
     * @param followerUserId  the ID of the follower user
     * @param followingUserId the ID of the user to be followed
     */
    @Transactional
    public void followUser(Long followerUserId, Long followingUserId) {
        if (!isFollow(followerUserId, followingUserId)) {
            Follower userFollower = new Follower();
            userFollower.setFollowerUserId(followerUserId);
            userFollower.setFollowingUserId(followingUserId);
            followerRepository.save(userFollower);
            log.info("User {} is now following user {}", followerUserId, followingUserId);
        }
    }

    /**
     * Allows a user to unfollow another user.
     *
     * @param followerUserId  the ID of the follower user
     * @param followingUserId the ID of the user to be unfollowed
     */
    @Transactional
    public void unfollowUser(Long followerUserId, Long followingUserId) {
        if (isFollow(followerUserId, followingUserId)) {
            followerRepository.deleteByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId);
            log.info("User {} unfollowed user {}", followerUserId, followingUserId);
        }
    }
}

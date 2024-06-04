package com.bntushniki.blog.service;

import com.bntushniki.blog.annotation.valid.PhoneValidator;
import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import com.bntushniki.blog.security.service.UserSecurityService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for work with user
 * @author Daniil
 */
@Service
@Slf4j
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserSecurityService userSecurityService;
    private final PhoneValidator phoneValidator;

    @Autowired
    public UserService(UserRepository userRepository, UserSecurityRepository userSecurityRepository,
                       UserSecurityService userSecurityService, PhoneValidator phoneValidator) {
        this.userRepository = userRepository;
        this.userSecurityService = userSecurityService;
        this.phoneValidator = phoneValidator;
    }

    /**
     * Retrieves all users.
     *
     * @return a list of users
     */
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user based on their ID.
     *
     * @param id the user ID
     * @return an optional user
     */
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Updates the first and last name of a user.
     *
     * @param user      the authenticated user
     * @param firstName the new first name
     * @param lastName  the new last name
     * @return true if the update was successful, false otherwise
     */
    @Transactional
    public boolean updateFirstLastName(User user, String firstName, String lastName) {
        if (firstName.isBlank() && lastName.isBlank()) {
            return false;
        }

        Optional<Users> usersOptional = getUsersByUserDetails(user);

        if (usersOptional.isEmpty()) {
            return false;
        }

        Users users = usersOptional.get();
        log.info("Updated first and last name for user: {}", users.getFirstName() + " " + users.getLastName());
        if (!firstName.isBlank()) {
            userRepository.updateFirstNameById(users.getUserId(), firstName);
        }
        if (!lastName.isBlank()) {
            userRepository.updateLastNameById(users.getUserId(), lastName);
        }
        return true;
    }

    /**
     * Updates the website and GitHub information of a user.
     *
     * @param user    the authenticated user
     * @param website the new website URL
     * @param github  the new GitHub URL
     * @return true if the update was successful, false otherwise
     */
    @Transactional
    public boolean updateResource(User user, String website, String github) {
        if (website.isBlank() && github.isBlank()) {
            return false;
        }

        Optional<Users> usersOptional = getUsersByUserDetails(user);

        if (usersOptional.isEmpty()) {
            return false;
        }

        Users users = usersOptional.get();

        if (!website.isBlank()) {
            userRepository.updateWebsiteById(users.getUserId(), website);
        }
        if (!github.isBlank()) {
            userRepository.updateGithubById(users.getUserId(), github);
        }
        log.info("Updated website and GitHub information for user: {}", users.getUserId());
        return true;
    }

    /**
     * Updates the phone number of a user.
     *
     * @param user  the authenticated user
     * @param phone the new phone number
     * @return true if the update was successful, false otherwise
     */
    @Transactional
    public boolean updatePhone(User user, String phone) {
        if (phone.isBlank() || !phoneValidator.phoneValid(phone)) {
            return false;
        }

        Optional<Users> usersOptional = getUsersByUserDetails(user);

        if (usersOptional.isEmpty()) {
            return false;
        }

        Users users = usersOptional.get();
        log.info("Updated phone information for user: {}", users.getUserId());
        return userRepository.updatePhoneById(users.getUserId(), phone) > 0;
    }

    /**
     * Searches for users based on a search query consisting of first and last name.
     *
     * @param searchQuery the search query
     * @return a list of users matching the query
     */
    public List<Users> searchUsers(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return List.of();
        }

        String[] parts = searchQuery.trim().split("\\s+");
        if (parts.length < 2) {
            return List.of();
        }

        String firstName = parts[0];
        String lastName = parts[1];

        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * Retrieves a map of users with their associated logins.
     *
     * @param usersList the list of users
     * @return a map of users with their logins
     */
    public Map<Users, String> getUserMapWithLogins(List<Users> usersList) {
        Map<Users, String> userMapWithLogins = new HashMap<>();
        for (Users user : usersList) {
            Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserId(user.getUserId());
            userSecurityOptional.ifPresent(userSecurity -> userMapWithLogins.put(user, userSecurity.getUserLogin()));
        }
        return userMapWithLogins;
    }

    private Optional<Users> getUsersByUserDetails(User user) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(user.getUsername());
        if (userSecurityOptional.isEmpty()) {
            return Optional.empty();
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        return getUserById(userSecurity.getUserId());
    }
}

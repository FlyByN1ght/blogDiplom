package com.bntushniki.blog.security.service;

import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.model.UserFaculty;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.annotation.valid.PasswordConstraintValidator;
import com.bntushniki.blog.security.model.UserRole;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing user security operations.
 * @author Daniil
 */
@Service
@Slf4j
public class UserSecurityService {

    private final PasswordEncoder passwordEncoder;
    private final UserSecurityRepository userSecurityRepository;
    private final UserRepository userRepository;
    private final PasswordConstraintValidator passwordConstraintValidator;

    @Autowired
    public UserSecurityService(PasswordEncoder passwordEncoder, UserSecurityRepository userSecurityRepository,
                               UserRepository userRepository, PasswordConstraintValidator passwordConstraintValidator) {
        this.passwordEncoder = passwordEncoder;
        this.userSecurityRepository = userSecurityRepository;
        this.userRepository = userRepository;
        this.passwordConstraintValidator = passwordConstraintValidator;
    }

    /**
     * Checks if a user exists by email.
     *
     * @param email the email to check
     * @return true if the user exists, otherwise false
     */
    public boolean userExistsByEmail(String email) {
        return userSecurityRepository.existsByEmail(email);
    }

    /**
     * Checks if a user exists by login.
     *
     * @param userLogin the login to check
     * @return true if the user exists, otherwise false
     */
    public boolean userExistsByLogin(String userLogin) {
        return userSecurityRepository.existsByUserLogin(userLogin);
    }

    /**
     * Finds a user security by login.
     *
     * @param userLogin the login to search for
     * @return an optional containing the user security, or empty if not found
     */
    public Optional<UserSecurity> findByUserLogin(String userLogin) {
        return userSecurityRepository.findByUserLogin(userLogin);
    }

    /**
     * Finds a user security by user ID.
     *
     * @param userId the user ID
     * @return an optional containing the user security, or empty if not found
     */
    public Optional<UserSecurity> findByUserId(Long userId) {
        return userSecurityRepository.getUserSecurityByUserId(userId);
    }

    /**
     * Registers a new user.
     *
     * @param userLogin    the user login
     * @param userPassword the user password
     * @param email        the email
     * @param lastName     the last name
     * @param firstName    the first name
     * @param faculty      the faculty
     * @param userRole     the user role
     * @return true if registration is successful, otherwise false
     */
    @Transactional
    public boolean registerUser(String userLogin, String userPassword, String email, String lastName, String firstName,
                                UserFaculty faculty, UserRole userRole) {
        if(userExistsByEmail(email) || userExistsByLogin(userLogin)) {
            return false;
        }

        Users user = new Users();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFaculty(faculty);
        user.setPhone("unspecified");
        user.setWebsite("unspecified");
        user.setGithub("unspecified");
        user.setAvatarUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5" +
                "/Windows_10_Default_Profile_Picture.svg/1024px-Windows_10_Default_Profile_Picture.svg.png?20221210150350");
        Users savedUser = userRepository.save(user);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUserLogin(userLogin);
        userSecurity.setUserPassword(passwordEncoder.encode(userPassword));
        userSecurity.setRole(userRole);
        userSecurity.setEmail(email);
        userSecurity.setUserId(savedUser.getUserId());
        userSecurityRepository.save(userSecurity);
        log.info("New user registered: {}", userLogin);
        return true;
    }

    /**
     * Validates user login and password.
     *
     * @param loginOrEmail the login or email
     * @param password     the password
     * @return true if the login is successful, otherwise false
     */
    public boolean loginAndPassword(String loginOrEmail, String password) {
        if (loginOrEmail.contains("@")) {
            Optional<UserSecurity> userOptional = userSecurityRepository.findByEmail(loginOrEmail);
            if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getUserPassword())) {
                return true;
            }
        } else {
            Optional<UserSecurity> userOptional = userSecurityRepository.findByUserLogin(loginOrEmail);
            if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getUserPassword())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a user has a teacher role.
     *
     * @param id the user ID
     * @return true if the user has a teacher role, otherwise false
     */
    public boolean checkRoleTeacher(Long id) {
        UserSecurity userSecurity = userSecurityRepository.getUserSecurityById(id);
        return userSecurity != null && userSecurity.getRole() == UserRole.TEACHER;
    }

    /**
     * Updates a user's password.
     *
     * @param user           the user
     * @param password       the new password
     * @param confirmPassword the confirmation of the new password
     * @return true if the password was updated successfully, otherwise false
     */
    @Transactional
    public boolean updatePassword(User user, String password, String confirmPassword) {
        if(password.isBlank() || confirmPassword.isBlank()) {
            return false;
        }
        if(!password.equals(confirmPassword)) {
            return false;
        }

        if(passwordConstraintValidator.passwordValid(password)) {
            return false;
        }

        Optional<UserSecurity> userSecurityOptional = findByUserLogin(user.getUsername());
        if (userSecurityOptional.isEmpty()) {
            return false;
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        return userSecurityRepository.updatePasswordById(userSecurity.getId(), passwordEncoder.encode(password)) > 0;
    }

    /**
     * Updates a user's email.
     *
     * @param user  the user
     * @param email the new email
     * @return true if the email was updated successfully, otherwise false
     */
    @Transactional
    public boolean updateEmail(User user, String email) {
        if(email.isBlank()) {
            return false;
        }
        if(userExistsByEmail(email)) {
            return false;
        }
        Optional<UserSecurity> userSecurityOptional = findByUserLogin(user.getUsername());
        if (userSecurityOptional.isEmpty()) {
            return false;
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        return userSecurityRepository.updateEmailById(userSecurity.getId(), email) > 0;
    }

    /**
     * Gets a map of users with their corresponding security details.
     *
     * @param usersList the list of users
     * @return a map of users with their security details
     */
    public Map<Users, UserSecurity> getUserMapWithSecurityDetails(List<Users> usersList) {
        Map<Users, UserSecurity> userMapWithSecurityDetails = new HashMap<>();
        for (Users user : usersList) {
            Optional<UserSecurity> userSecurityOptional = findByUserId(user.getUserId());
            userSecurityOptional.ifPresent(userSecurity -> userMapWithSecurityDetails.put(user, userSecurity));
        }
        return userMapWithSecurityDetails;
    }

    /**
     * Toggles a user's role between student and admin.
     *
     * @param userId the user ID
     */
    @Transactional
    public void toggleUserRoleStudentToAdmin(Long userId) {
        Optional<UserSecurity> userSecurityOptional = findByUserId(userId);
        if (userSecurityOptional.isPresent()) {
            UserSecurity user = userSecurityOptional.get();
            if (user.getRole().equals(UserRole.ADMIN)) {
                userSecurityRepository.updateRoleById(userId, UserRole.STUDENT);
                log.info("User role toggled to STUDENT: {}", userId);
            } else if (user.getRole().equals(UserRole.STUDENT)) {
                userSecurityRepository.updateRoleById(userId, UserRole.ADMIN);
                log.info("User role toggled to ADMIN: {}", userId);
            }
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     */
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        userSecurityRepository.deleteByUserId(id);
        log.info("User deleted: {}", id);
    }
}

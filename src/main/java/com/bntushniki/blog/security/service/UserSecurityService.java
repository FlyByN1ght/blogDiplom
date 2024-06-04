package com.bntushniki.blog.security.service;

import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.model.UserFaculty;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.annotation.valid.PasswordConstraintValidator;
import com.bntushniki.blog.security.model.UserRole;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserSecurityService {

    private final PasswordEncoder passwordEncoder;
    private final UserSecurityRepository userSecurityRepository;
    private final UserRepository userRepository;
    private final PasswordConstraintValidator passwordConstraintValidator;

    @Autowired
    public UserSecurityService(PasswordEncoder passwordEncoder, UserSecurityRepository userSecurityRepository, UserRepository userRepository, PasswordConstraintValidator passwordConstraintValidator) {
        this.passwordEncoder = passwordEncoder;
        this.userSecurityRepository = userSecurityRepository;
        this.userRepository = userRepository;
        this.passwordConstraintValidator = passwordConstraintValidator;
    }

    public boolean userExistsByEmail(String email) {
        return userSecurityRepository.existsByEmail(email) ;
    }

    public boolean userExistsByLogin(String userLogin) {
        return userSecurityRepository.existsByUserLogin(userLogin);
    }

    public Optional<UserSecurity> findByUserLogin(String userLogin) {
        return userSecurityRepository.findByUserLogin(userLogin);
    }

    public Optional<UserSecurity> findByUserId(Long userId) {
        return userSecurityRepository.getUserSecurityByUserId(userId);
    }

    @Transactional
    public boolean registerUser(String userLogin, String userPassword, String email, String lastName, String firstName,
                                 UserFaculty faculty) {
        if(userExistsByEmail(email) || userExistsByLogin(userLogin)) {
            return false;
        }

        Users user = new Users();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFaculty(faculty);
        user.setAvatarUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5" +
                "/Windows_10_Default_Profile_Picture.svg/1024px-Windows_10_Default_Profile_Picture.svg.png?20221210150350");
        Users savedUser = userRepository.save(user);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUserLogin(userLogin);
        userSecurity.setUserPassword(passwordEncoder.encode(userPassword));
        userSecurity.setRole(UserRole.STUDENT);
        userSecurity.setEmail(email);
        userSecurity.setUserId(savedUser.getUserId());
        userSecurityRepository.save(userSecurity);
        return true;
    }

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

    public boolean checkRoleTeacher(Long id) {
        UserSecurity userSecurity = userSecurityRepository.getUserSecurityById(id);
        return userSecurity != null && userSecurity.getRole() == UserRole.TEACHER;
    }

    @Transactional
    public boolean updatePassword (User user, String password, String confirmPassword) {
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


    public Map<Users, UserSecurity> getUserMapWithSecurityDetails(List<Users> usersList) {
        Map<Users, UserSecurity> userMapWithSecurityDetails = new HashMap<>();
        for (Users user : usersList) {
            Optional<UserSecurity> userSecurityOptional = findByUserId(user.getUserId());
            userSecurityOptional.ifPresent(userSecurity -> userMapWithSecurityDetails.put(user, userSecurity));
        }
        return userMapWithSecurityDetails;
    }

    @Transactional
    public void toggleUserRole(Long userId) {
        Optional<UserSecurity> userSecurityOptional = findByUserId(userId);
        if (userSecurityOptional.isPresent()) {
            UserSecurity user = userSecurityOptional.get();
            if (user.getRole().equals(UserRole.ADMIN)) {
                userSecurityRepository.updateRoleById(userId, UserRole.STUDENT);
            } else if (user.getRole().equals(UserRole.STUDENT)) {
                userSecurityRepository.updateRoleById(userId, UserRole.ADMIN);
            }
        }
    }
}

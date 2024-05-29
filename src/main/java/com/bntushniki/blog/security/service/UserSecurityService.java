package com.bntushniki.blog.security.service;

import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.model.UserFaculty;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.model.UserRole;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserSecurityService {

    private final PasswordEncoder passwordEncoder;
    private final UserSecurityRepository userSecurityRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserSecurityService(PasswordEncoder passwordEncoder, UserSecurityRepository userSecurityRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userSecurityRepository = userSecurityRepository;
        this.userRepository = userRepository;
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

    @Transactional(rollbackFor = Exception.class)
    public boolean registerUser(String userLogin, String userPassword, String email, String lastName, String firstName,
                                 UserFaculty faculty) {
        if(userExistsByEmail(email) || userExistsByLogin(userLogin)) {
            return false;
        }

        Users user = new Users();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFaculty(faculty);
        user.setGithub("unspecified");
        user.setWebsite("unspecified");
        user.setPhone("unspecified");
        user.setAvatarUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5" +
                "/Windows_10_Default_Profile_Picture.svg/1024px-Windows_10_Default_Profile_Picture.svg.png?20221210150350");
        Users savedUser = userRepository.save(user);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUserLogin(userLogin);
        userSecurity.setUserPassword(passwordEncoder.encode(userPassword));
        userSecurity.setRole(UserRole.STUDENT);
        userSecurity.setEmail(email);
        userSecurity.setUserId(savedUser.getUserId());
        userSecurity.setIsBlocked(false);
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

    public boolean checkRole(Long id) {
        UserSecurity userSecurity = userSecurityRepository.getUserSecurityById(id);
        return userSecurity != null && userSecurity.getRole() == UserRole.TEACHER;
    }
}

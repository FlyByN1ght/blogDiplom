package com.bntushniki.blog.security.service;

import com.bntushniki.blog.model.User;
import com.bntushniki.blog.model.UserFaculty;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.model.UserRole;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.model.dto.RegistrationDto;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Transactional(rollbackFor = Exception.class)
    public boolean registerUser(String userLogin, String userPassword, String email, String lastName, String firstName,
                                 UserFaculty faculty) {
        if(userExistsByEmail(email) || userExistsByLogin(userLogin)) {
            return false;
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFaculty(faculty);
        User savedUser = userRepository.save(user);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUserLogin(userLogin);
        userSecurity.setUserPassword(passwordEncoder.encode(userPassword));
        userSecurity.setRole(UserRole.Student);
        userSecurity.setEmail(email);
        userSecurity.setUserId(savedUser.getUserId());
        userSecurity.setIsBlocked(false);
        userSecurityRepository.save(userSecurity);
        return true;
    }

    public boolean login(String loginOrEmail, String password) {
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
}

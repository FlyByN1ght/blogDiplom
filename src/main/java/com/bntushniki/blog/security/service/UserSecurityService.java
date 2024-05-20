package com.bntushniki.blog.security.service;

import com.bntushniki.blog.model.User;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.model.UserRole;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.model.dto.RegistrationDto;
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

    @Transactional(rollbackFor = Exception.class)
    public boolean registerUser(RegistrationDto registrationDto) {
        Optional<UserSecurity> security = userSecurityRepository.findByUserLogin(registrationDto.getUserLogin());
        if (security.isPresent()) {
            //throw new SameUserInDatabase(registrationDto.getLogin());
        }
        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setFaculty(registrationDto.getFaculty());
        User savedUser = userRepository.save(user);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUserLogin(registrationDto.getUserLogin());
        userSecurity.setUserPassword(passwordEncoder.encode(registrationDto.getUserPassword()));
        userSecurity.setRole(UserRole.Student);
        userSecurity.setEmail(registrationDto.getEmail());
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

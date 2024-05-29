package com.bntushniki.blog.service;

import com.bntushniki.blog.annotation.ValidPhone;
import com.bntushniki.blog.annotation.valid.PhoneValidator;
import com.bntushniki.blog.model.Users;
import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.annotation.valid.PasswordConstraintValidator;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import com.bntushniki.blog.security.service.UserSecurityService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSecurityService userSecurityService;
    private final UserSecurityRepository userSecurityRepository;
    private final PhoneValidator phoneValidator;
    private final PasswordConstraintValidator passwordConstraintValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserSecurityRepository userSecurityRepository, PasswordEncoder passwordEncoder, UserSecurityService userSecurityService, UserSecurityRepository userSecurityRepository1, PhoneValidator phoneValidator, PasswordConstraintValidator passwordConstraintValidator, PasswordEncoder passwordEncoder1) {
        this.userRepository = userRepository;
        this.userSecurityService = userSecurityService;
        this.userSecurityRepository = userSecurityRepository1;
        this.phoneValidator = phoneValidator;
        this.passwordConstraintValidator = passwordConstraintValidator;
        this.passwordEncoder = passwordEncoder;
    }

    private Optional<Users> getUsersByUserDetails(User user) {
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(user.getUsername());
        if (userSecurityOptional.isEmpty()) {
            return Optional.empty();
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        return getUserById(userSecurity.getUserId());
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

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

        if (!firstName.isBlank()) {
            userRepository.updateFirstNameById(users.getUserId(), firstName);
        }
        if (!lastName.isBlank()) {
            userRepository.updateLastNameById(users.getUserId(), lastName);
        }
        return true;
    }

    @Transactional
    public boolean updateEmail(User user, String email) {
        if(email.isBlank()) {
            return false;
        }
        if(userSecurityService.userExistsByEmail(email)) {
            return false;
        }
        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(user.getUsername());
        if (userSecurityOptional.isEmpty()) {
            return false;
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        return userSecurityRepository.updateEmailById(userSecurity.getId(), email) > 0;
    }

    @Transactional
    public boolean updateResource (User user, String website, String github) {
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
        return true;
    }

    @Transactional
    public boolean updatePhone (User user, String phone) {
        if(phone.isBlank() || !phoneValidator.phoneValid(phone) ) {
            return false;
        }

        Optional<Users> usersOptional = getUsersByUserDetails(user);

        if (usersOptional.isEmpty()) {
            return false;
        }

        Users users = usersOptional.get();

        return userRepository.updatePhoneById(users.getUserId(), phone) > 0;
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

        Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserLogin(user.getUsername());
        if (userSecurityOptional.isEmpty()) {
            return false;
        }

        UserSecurity userSecurity = userSecurityOptional.get();
        return userSecurityRepository.updatePasswordById(userSecurity.getId(), passwordEncoder.encode(password)) > 0;
    }
}

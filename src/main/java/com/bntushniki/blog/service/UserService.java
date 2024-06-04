package com.bntushniki.blog.service;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSecurityService userSecurityService;
    private final UserSecurityRepository userSecurityRepository;
    private final PhoneValidator phoneValidator;

    @Autowired
    public UserService(UserRepository userRepository, UserSecurityRepository userSecurityRepository,
                       UserSecurityService userSecurityService, PhoneValidator phoneValidator) {
        this.userRepository = userRepository;
        this.userSecurityService = userSecurityService;
        this.userSecurityRepository = userSecurityRepository;
        this.phoneValidator = phoneValidator;
    }

    public List<Users> findAll() {
        return userRepository.findAll();
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

    public Map<Users, String> getUserMapWithLogins(List<Users> usersList) {
        Map<Users, String> userMapWithLogins = new HashMap<>();
        for (Users user : usersList) {
            Optional<UserSecurity> userSecurityOptional = userSecurityService.findByUserId(user.getUserId());
            userSecurityOptional.ifPresent(userSecurity -> userMapWithLogins.put(user, userSecurity.getUserLogin()));
        }
        return userMapWithLogins;
    }
}

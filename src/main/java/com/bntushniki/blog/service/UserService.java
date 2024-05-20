package com.bntushniki.blog.service;

import com.bntushniki.blog.repository.UserRepository;
import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSecurityRepository userSecurityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserSecurityRepository userSecurityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userSecurityRepository = userSecurityRepository;
        this.passwordEncoder = passwordEncoder;
    }
}

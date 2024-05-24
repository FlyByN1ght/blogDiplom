package com.bntushniki.blog.security.service;

import com.bntushniki.blog.security.model.UserSecurity;
import com.bntushniki.blog.security.repository.UserSecurityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserSecurityRepository userSecurityRepository;

    @Autowired
    public CustomUserDetailService(UserSecurityRepository userSecurityRepository) {
        this.userSecurityRepository = userSecurityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
        Optional<UserSecurity> securityInfoOptional = userSecurityRepository.findByUserLogin(loginOrEmail);
        if (securityInfoOptional.isEmpty()) {
            securityInfoOptional = userSecurityRepository.findByEmail(loginOrEmail);
        }
        if (securityInfoOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username or email not found: " + loginOrEmail);
        }
        UserSecurity security = securityInfoOptional.get();
        return User.builder()
                .username(security.getUserLogin())
                .password(security.getUserPassword())
                .roles(security.getRole().toString())
                .build();
    }
}

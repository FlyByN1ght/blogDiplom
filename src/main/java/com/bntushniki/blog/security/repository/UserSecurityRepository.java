package com.bntushniki.blog.security.repository;

import com.bntushniki.blog.security.model.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {
    Optional<UserSecurity> findByUserLogin(String userLogin);
    boolean existsByEmail(String email);
    boolean existsByUserLogin(String userLogin);
    Optional<UserSecurity> findByEmail(String loginOrEmail);
}

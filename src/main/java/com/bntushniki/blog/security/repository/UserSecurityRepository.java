package com.bntushniki.blog.security.repository;

import com.bntushniki.blog.security.model.UserRole;
import com.bntushniki.blog.security.model.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {
    Optional<UserSecurity> findByUserLogin(String userLogin);
    boolean existsByEmail(String email);
    boolean existsByUserLogin(String userLogin);
    Optional<UserSecurity> findByEmail(String loginOrEmail);
    UserSecurity getUserSecurityById(Long id);
    Optional<UserSecurity> getUserSecurityByUserId(Long userId);
    void deleteByUserId(Long id);

    @Modifying
    @Query("UPDATE user_security u SET u.email = :email WHERE u.id = :id")
    int updateEmailById(@Param("id") Long id, @Param("email") String email);

    @Modifying
    @Query("UPDATE user_security  u SET u.userPassword = :password WHERE u.id = :id")
    int updatePasswordById(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("UPDATE user_security u SET u.role = :role WHERE u.id = :id")
    void updateRoleById(@Param("id") Long id, @Param("role") UserRole role);
}

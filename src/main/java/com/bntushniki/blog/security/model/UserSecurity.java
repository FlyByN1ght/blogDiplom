package com.bntushniki.blog.security.model;

import com.bntushniki.blog.security.annotation.ExactValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;


@Entity(name = "user_security")
public class UserSecurity {
    @Id
    @SequenceGenerator(name = "secSeqGen", sequenceName = "user_security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "secSeqGen")
    private Long id;

    @Column(name = "user_login")
    @ExactValue
    private Integer userLogin;

    @Column(name = "user_password")
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_security_role")
    private UserRole role;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_blocked")
    private Boolean isBlocked;
}

package com.bntushniki.blog.security.model;

import com.bntushniki.blog.security.annotation.ExactValue;
import com.bntushniki.blog.security.annotation.ValidPassword;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_security")
public class UserSecurity {
    @Id
    @SequenceGenerator(name = "secSeqGen", sequenceName = "user_security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "secSeqGen")
    private Long id;

    @Column(name = "user_login", unique = true)
    @ExactValue
    @NotNull
    private String userLogin;

    @Column(name = "user_password")
    @NotBlank
    @ValidPassword
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_security_role")
    private UserRole role;

    @Column(name = "user_id")
    private Long userId;

    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}

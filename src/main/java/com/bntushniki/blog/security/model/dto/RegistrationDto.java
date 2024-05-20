package com.bntushniki.blog.security.model.dto;

import com.bntushniki.blog.model.UserFaculty;
import com.bntushniki.blog.security.annotation.ExactValue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {

    private String userLogin;
    private String userPassword;
    private String email;
    private String lastName;
    private String firstName;
    private UserFaculty faculty;
}

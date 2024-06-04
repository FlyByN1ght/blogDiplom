package com.bntushniki.blog.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class entity for users
 * @author Daniil
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class Users {
    @Id
    @SequenceGenerator(name = "userSeqGen", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "userSeqGen")
    private Long userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "faculty")
    private UserFaculty faculty;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "website")
    private String website;

    @Column(name = "github")
    private String github;

    @Column(name = "phone")
    private String phone;
}

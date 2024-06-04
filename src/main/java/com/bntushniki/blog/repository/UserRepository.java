package com.bntushniki.blog.repository;

import com.bntushniki.blog.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Modifying
    @Query("UPDATE users u SET u.firstName = :firstName WHERE u.userId = :id")
    int updateFirstNameById(@Param("id") Long id, @Param("firstName") String firstName);

    @Modifying
    @Query("UPDATE users u SET u.lastName = :lastName WHERE u.userId = :id")
    int updateLastNameById(@Param("id") Long id, @Param("lastName") String lastName);

    @Modifying
    @Query("UPDATE users u SET u.website = :website WHERE u.userId = :id")
    int updateWebsiteById(@Param("id") Long id, @Param("website") String website);

    @Modifying
    @Query("UPDATE users u SET u.github = :github WHERE u.userId = :id")
    int updateGithubById(@Param("id") Long id, @Param("github") String github);

    @Modifying
    @Query("UPDATE users u SET u.phone = :phone WHERE u.userId = :id")
    int updatePhoneById(@Param("id") Long id, @Param("phone") String phone);

    List<Users> findByFirstNameAndLastName(String firstName, String lastName);
}

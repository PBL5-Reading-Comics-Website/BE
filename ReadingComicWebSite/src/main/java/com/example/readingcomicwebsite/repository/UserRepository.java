package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u.password FROM User u WHERE u.username = :username", nativeQuery = true)
    Optional<User> findPasswordByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM user WHERE user.email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE user SET username = :username, email = :email, password = :password WHERE id = :id", nativeQuery = true)
    void updateUserInfo(@Param("username") String username, @Param("email") String email, @Param("password") String password, @Param("id") Integer id);

    @Query(value = "SELECT * FROM user WHERE id = :id", nativeQuery = true)
    Optional<User> findUserById(@Param("id") Integer id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}

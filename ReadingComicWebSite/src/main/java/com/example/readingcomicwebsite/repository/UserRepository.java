package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM user WHERE user.username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "SELECT u.password FROM User u WHERE u.username = :username", nativeQuery = true)
    Optional<User> findPasswordByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM user WHERE user.email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    //update user info
    @Query(value = "UPDATE user SET user.username = :username, user.email = :email, user.password = :password WHERE user.id = :id RETURNING *", nativeQuery = true)
    Optional<User> updateUserInfo(@Param("username") String username, @Param("email") String email, @Param("password") String password, @Param("id") Integer id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}

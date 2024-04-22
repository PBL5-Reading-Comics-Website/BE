package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM user WHERE user.username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);
    @Query("SELECT u.password FROM User u WHERE u.username = ?1")
    String findPasswordByUsername( String username);
}

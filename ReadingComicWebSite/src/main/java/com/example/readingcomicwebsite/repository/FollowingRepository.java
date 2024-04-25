package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Following;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Integer> {
    // find all by user id
    List<Following> findByUserId(Integer userId);
}

package com.example.readingcomicwebsite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.readingcomicwebsite.model.Following;

public interface FollowingRepository extends JpaRepository<Following, Integer> {
    Page<Following> findByUserId(Integer userId, Pageable pageable);
}
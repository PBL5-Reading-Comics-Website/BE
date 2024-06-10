package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.readingcomicwebsite.model.Following;

public interface FollowingRepository extends JpaRepository<Following, Integer> {
    Page<Following> findByUserId(Integer userId, Pageable pageable);
    boolean existsByUser_IdAndManga_Id(Integer userId, Integer mangaId);
    void deleteByUserAndManga(User user, Manga manga);
}
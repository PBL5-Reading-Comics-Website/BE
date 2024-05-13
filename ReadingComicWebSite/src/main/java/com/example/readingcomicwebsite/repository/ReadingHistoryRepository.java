package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.ReadingHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Integer> {
    List<ReadingHistory> findAllByUserId(Integer userId);

    Page<ReadingHistory> findByUserId(Integer userId, Pageable Pageable);

    @Query(value = "SELECT m.name AS manga_name, m.view_number AS view_count, GROUP_CONCAT(t.name) AS tags " +
            "FROM reading_history rh " +
            "JOIN Manga m ON rh.manga_id = m.id " +
            "JOIN manga_tag mt ON m.id = mt.manga_id " +
            "JOIN Tag t ON mt.tag_id = t.id " +
            "WHERE rh.user_id = :userId " +
            "GROUP BY m.id", nativeQuery = true)
    List<Object[]> getReadManga(@Param("userId") Integer userId);
}
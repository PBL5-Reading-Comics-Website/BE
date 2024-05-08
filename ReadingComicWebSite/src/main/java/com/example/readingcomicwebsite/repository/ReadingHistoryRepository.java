package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.ReadingHistory;
import com.example.readingcomicwebsite.util.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Integer> {
    // find all reading history by user id
    List<ReadingHistory> findAllByUserId(Integer userId);

    Page<ReadingHistory> findByUserId(Integer userId, Pageable Pageable);
}

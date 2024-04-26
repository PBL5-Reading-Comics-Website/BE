package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Long>{
    // find all reading history by user id
    List<ReadingHistory> findAllByUserId(Long userId);
}

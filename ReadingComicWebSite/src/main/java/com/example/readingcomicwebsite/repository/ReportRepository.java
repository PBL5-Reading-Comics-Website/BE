package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    Report findByComment(Comment comment);
}

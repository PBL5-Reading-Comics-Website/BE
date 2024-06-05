package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReportService {
    Page<Report> findAll(Pageable pageable);

    Report findById(Integer id);

    Report findByComment(Comment comment);

    Report save(Report report);

    Report update(Integer id, Report report);

    void deleteById(Integer id);

    Report add(Report report);

    Boolean acceptReport(Integer id);
}

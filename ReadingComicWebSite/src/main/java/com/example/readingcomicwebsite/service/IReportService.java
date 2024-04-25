package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Report;

import java.util.List;

public interface IReportService {
    List<Report> findAll();

    Report findById(Long id);

    Report save(Report report);

    Report update(Long id, Report report);

    void deleteById(Long id);
}

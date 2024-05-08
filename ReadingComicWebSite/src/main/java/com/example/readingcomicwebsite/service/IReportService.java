package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Report;

import java.util.List;

public interface IReportService {
    List<Report> findAll();

    Report findById(Integer id);

    Report save(Report report);

    Report update(Integer id, Report report);

    void deleteById(Integer id);

    Report add(Report report);
}

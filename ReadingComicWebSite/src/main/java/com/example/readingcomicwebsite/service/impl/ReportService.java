package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Report;
import com.example.readingcomicwebsite.repository.ReportRepository;
import com.example.readingcomicwebsite.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final ReportRepository repository;

    @Override
    public List<Report> findAll() {
        return repository.findAll();
    }

    @Override
    public Report findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Report save(Report report) {
        return repository.save(report);
    }

    @Override
    public Report update(Long id, Report report) {
        Report reportDb = repository.findById(id).orElse(null);
        if (reportDb == null) {
            return null;
        }
        BeanUtils.copyProperties(report, reportDb);
        return repository.save(reportDb);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

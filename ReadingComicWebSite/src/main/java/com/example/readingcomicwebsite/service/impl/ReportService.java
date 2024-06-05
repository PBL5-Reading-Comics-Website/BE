package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Report;
import com.example.readingcomicwebsite.repository.ReportRepository;
import com.example.readingcomicwebsite.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final ReportRepository repository;
    private final CommentService commentService;

    @Override
    public Page<Report> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Report findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Report findByComment(Comment comment) {
        return repository.findByComment(comment);
    }

    @Override
    public Report save(Report report) {
        return repository.save(report);
    }

    @Override
    public Report update(Integer id, Report report) {
        Report reportDb = repository.findById(id).orElse(null);
        if (reportDb == null) {
            return null;
        }
        BeanUtils.copyProperties(report, reportDb);
        return repository.save(reportDb);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Report add(Report report) {
        return repository.save(report);
    }

    @Override
    public Boolean acceptReport(Integer id) {
        Report report = repository.findById(id).orElse(null);
        if (report == null) {
            return false;
        }
        Integer commentId = report.getComment().getId();
        repository.deleteById(id);
        commentService.deleteById(commentId);
        return true;
    }
}

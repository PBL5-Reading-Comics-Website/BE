package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.ReadingHistory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IReadingHistoryService {
    List<ReadingHistory> findAll();

    ReadingHistory findById(Integer id);

    ReadingHistory add(ReadingHistory readingHistory);

    ReadingHistory update(Integer id, ReadingHistory readingHistory);

    void deleteById(Integer id);

    List<ReadingHistory> findAllByUserId(Integer userId);

    Page<ReadingHistory> findByUserId(Integer userId, String sortField, String sortOrder, Integer page, Integer size);

    List<Object[]> getReadManga(Integer userId);
}

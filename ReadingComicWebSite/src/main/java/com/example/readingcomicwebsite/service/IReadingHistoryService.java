package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.ReadingHistory;

import java.util.List;

public interface IReadingHistoryService {
    List<ReadingHistory> findAll();

    ReadingHistory findById(Long id);

    ReadingHistory add(ReadingHistory readingHistory);

    ReadingHistory update(Long id, ReadingHistory readingHistory);

    void deleteById(Long id);

    List<ReadingHistory> findAllByUserId(Long userId);
}

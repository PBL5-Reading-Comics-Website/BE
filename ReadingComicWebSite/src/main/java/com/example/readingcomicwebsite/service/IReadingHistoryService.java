package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.ReadingHistory;

import java.util.List;

public interface IReadingHistoryService {
    List<ReadingHistory> findAll();

    ReadingHistory findById(Integer id);

    ReadingHistory add(ReadingHistory readingHistory);

    ReadingHistory update(Integer id, ReadingHistory readingHistory);

    void deleteById(Integer id);

    List<ReadingHistory> findAllByUserId(Integer userId);
}

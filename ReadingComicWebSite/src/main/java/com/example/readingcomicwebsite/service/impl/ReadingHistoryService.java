package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.ReadingHistory;
import com.example.readingcomicwebsite.repository.ReadingHistoryRepository;
import com.example.readingcomicwebsite.service.IReadingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingHistoryService implements IReadingHistoryService {
    private final ReadingHistoryRepository repository;
    @Override
    public List<ReadingHistory> findAll() {
        return repository.findAll();
    }

    @Override
    public ReadingHistory findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ReadingHistory add(ReadingHistory readingHistory) {
        if (readingHistory.getChapter() == null || readingHistory.getChapter().getId() == null)
            return null;
        if (readingHistory.getManga() == null || readingHistory.getManga().getId() == null)
            return null;
        if (readingHistory.getUser() == null || readingHistory.getUser().getId() == null)
            return null;
        return repository.save(readingHistory);
    }

    @Override
    public ReadingHistory update(Long id, ReadingHistory readingHistory) {
        ReadingHistory readingHistoryDb = repository.findById(id).orElse(null);
        if (readingHistoryDb == null)
            return null;
        BeanUtils.copyProperties(readingHistory, readingHistoryDb);
        return repository.save(readingHistoryDb);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ReadingHistory> findAllByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }
}

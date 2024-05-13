package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.ReadingHistory;
import com.example.readingcomicwebsite.repository.ReadingHistoryRepository;
import com.example.readingcomicwebsite.repository.UserRepository;
import com.example.readingcomicwebsite.service.IReadingHistoryService;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingHistoryService implements IReadingHistoryService {
    private final ReadingHistoryRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<ReadingHistory> findAll() {
        return repository.findAll();
    }

    @Override
    public ReadingHistory findById(Integer id) {
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
    public ReadingHistory update(Integer id, ReadingHistory readingHistory) {
        ReadingHistory readingHistoryDb = repository.findById(id).orElse(null);
        if (readingHistoryDb == null)
            return null;
        BeanUtils.copyProperties(readingHistory, readingHistoryDb);
        return repository.save(readingHistoryDb);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ReadingHistory> findAllByUserId(Integer userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public Page<ReadingHistory> findByUserId(Integer userId, String sortField, String sortOrder, Integer page,
                                             Integer size) {
        Pageable pageable = PageUtils.makePageRequest(sortField, sortOrder, page, size);
        return repository.findByUserId(userId, pageable);
    }

    @Override
    public List<Object[]> getReadManga(Integer userId) {
        return repository.getReadManga(userId);
    }
}

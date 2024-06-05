package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Waiting;
import org.springframework.data.domain.Page;

public interface IWaitingService {
    Page<Waiting> findAll(Integer page, Integer size);

    Waiting findById(Integer id);

    Waiting findByUserId(Integer userId);

    Waiting add(Waiting waiting);

    void deleteById(Integer id);

    void acceptWaiting(Integer id);

    void rejectWaiting(Integer id);
}

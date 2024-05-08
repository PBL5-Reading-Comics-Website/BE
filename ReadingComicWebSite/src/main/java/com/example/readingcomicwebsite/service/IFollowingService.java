package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Following;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IFollowingService {
    Page<Following> findAll(String sortField, String sortOrder, Integer page, Integer size);

    Following findById(Integer id);

    Following add(Following following);

    Following update(Integer id, Following following);

    void deleteById(Integer id);

    Page<Following> findByUserId(Integer userId, String sortField, String sortOrder, Integer page, Integer size);
}

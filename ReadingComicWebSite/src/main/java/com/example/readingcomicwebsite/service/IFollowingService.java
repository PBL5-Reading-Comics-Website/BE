package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Following;

import java.util.List;

public interface IFollowingService {
    List<Following> findAll();

    Following findById(Integer id);

    Following add(Following following);

    Following update(Integer id, Following following);

    void deleteById(Integer id);

    List<Following> findByUserId(Integer userId);
}

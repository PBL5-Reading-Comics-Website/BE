package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Following;
import com.example.readingcomicwebsite.repository.FollowingRepository;
import com.example.readingcomicwebsite.service.IFollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowingService implements IFollowingService {
    private final FollowingRepository repository;

    @Override
    public List<Following> findAll() {
        return repository.findAll();
    }

    @Override
    public Following findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Following add(Following following) {
        if (following.getUser() == null || following.getManga() == null)
            return null;
        return repository.save(following);
    }

    @Override
    public Following update(Integer id, Following following) {
        Following followingDb = repository.findById(id).orElse(null);
        if (followingDb == null)
            return null;
        BeanUtils.copyProperties(following, followingDb);
        return repository.save(followingDb);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Following> findByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }
}

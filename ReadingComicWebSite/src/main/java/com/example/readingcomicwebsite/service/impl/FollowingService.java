package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Following;
import com.example.readingcomicwebsite.repository.FollowingRepository;
import com.example.readingcomicwebsite.service.IFollowingService;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowingService implements IFollowingService {
    private final FollowingRepository repository;

    @Override
    public Page<Following> findAll(String sortField, String sortOrder, Integer page, Integer size) {
        return repository.findAll(PageUtils.makePageRequest(sortField, sortOrder, page, size));
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
    public Page<Following> findByUserId(Integer userId, String sortField, String sortOrder, Integer page, Integer size) {
        Pageable pageable = PageUtils.makePageRequest(sortField, sortOrder, page, size);
        return repository.findByUserId(userId, pageable);
    }
}

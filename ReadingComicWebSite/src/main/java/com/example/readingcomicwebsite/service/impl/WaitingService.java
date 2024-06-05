package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.model.Waiting;
import com.example.readingcomicwebsite.repository.WaitingRepository;
import com.example.readingcomicwebsite.service.IWaitingService;
import com.example.readingcomicwebsite.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingService implements IWaitingService {
    private final WaitingRepository repository;
    private final UserService userService;

    @Override
    public Page<Waiting> findAll(Integer page, Integer size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Waiting findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Waiting findByUserId(Integer userId) {
        return repository.findByUser(userService.findById(userId));
    }

    @Override
    public Waiting add(Waiting waiting) {
        Waiting waitingDb = repository.findByUser(waiting.getUser());
        if (waitingDb != null) {
            return null;
        }
        User userDb = waiting.getUser();
        if (userDb == null) {
            return null;
        }
        return repository.save(waiting);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void acceptWaiting(Integer id) {
        Waiting waiting = repository.findById(id).orElse(null);
        if (waiting == null) {
            return;
        }
        User user = waiting.getUser();
        if (user == null) {
            return;
        }
        user.setRole(Role.POSTER);
        repository.deleteById(id);
    }

    @Override
    public void rejectWaiting(Integer id) {
        repository.deleteById(id);
    }
}

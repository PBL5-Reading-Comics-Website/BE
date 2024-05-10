package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.repository.UserRepository;
import com.example.readingcomicwebsite.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository repository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

//    @Override
//    public Optional<User> findByUsername(String username) {
//        return repository.findByUsername(username);
//    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public User findPasswordByUsername(String username) {
        return repository.findPasswordByUsername(username).orElse(null);
    }

    @Override
    public User add(User user) {
        if (repository.findByUsername(user.getUsername()).isEmpty() && repository.findByEmail(user.getEmail()).isEmpty())
            return repository.save(user);
        return null;
    }

    @Override
    public User update(Integer id, User user) {
        User userDb = repository.findById(id).orElse(null);
        if (userDb == null)
            return null;
        BeanUtils.copyProperties(user, userDb);
        return repository.save(userDb);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public boolean isDelete(String username) {
        if (repository.findByUsername(username).isEmpty())
            return true;
        return false;
    }

    @Override
    public boolean checkExistEmail(String email) {
        if (repository.findByEmail(email).isEmpty())
            return false;
        return true;
    }

    @Override
    public boolean checkExistUsername(String username) {
        if (repository.findByUsername(username).isEmpty())
            return false;
        return true;
    }

    @Override
    public User getInfo(String username) {
        return repository.findByUsername(username).orElse(null);
    }
}

package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> findAll();

    User findById(Integer id);

    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    User findPasswordByUsername(String username);

    User add(User user);

    User update(Integer id, User user);

    void deleteById(Integer id);

    boolean isDelete(String username);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

}

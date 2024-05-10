package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    Page<User> findAll(Pageable pageable);

    User findById(Integer id);

    User findByEmail(String email);

    User findPasswordByUsername(String username);

    User add(User user);

    User update(Integer id, User user);

    void deleteById(Integer id);

    boolean isDelete(String username);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    User getInfo(String username);
}

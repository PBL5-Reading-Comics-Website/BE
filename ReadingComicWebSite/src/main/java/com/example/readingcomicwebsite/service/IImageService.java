package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Chapter;
import com.example.readingcomicwebsite.model.Image;

import java.util.List;
import java.util.Optional;

public interface IImageService {
    List<Image> list(Chapter chapter);
    Optional<Image> getOne(Integer id);
    void save(Image image);
    void delete(Integer id);
    boolean isExist(Integer id);
}

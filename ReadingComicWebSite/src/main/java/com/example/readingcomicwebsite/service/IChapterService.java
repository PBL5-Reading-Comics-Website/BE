package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Chapter;

import java.util.List;

public interface IChapterService {
    List<Chapter> findAll();

    Chapter findById(Integer id);

    Chapter add(Chapter chapter);

    Chapter update(Integer id, Chapter chapter);

    void deleteById(Integer id);

    List<Chapter> findAllByMangaId(Integer mangaId);

    boolean isChapterExist(Integer id);
}

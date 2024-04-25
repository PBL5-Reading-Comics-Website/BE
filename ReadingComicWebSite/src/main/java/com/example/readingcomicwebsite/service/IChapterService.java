package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Chapter;

import java.util.List;

public interface IChapterService {
    List<Chapter> findAll();

    Chapter findById(Long id);

    Chapter add(Chapter chapter);

    Chapter update(Long id, Chapter chapter);

    void deleteById(Long id);

    List<Chapter> findAllByMangaId(Long mangaId);
}

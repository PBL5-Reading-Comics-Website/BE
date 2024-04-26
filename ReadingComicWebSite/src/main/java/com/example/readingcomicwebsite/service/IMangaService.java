package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Manga;

import java.util.List;

public interface IMangaService {
    List<Manga> findAll();

    Manga findById(Long id);

    List<Manga> findByName(String name);

    Manga add(Manga manga);

    Manga update(Long id, Manga manga);

    void deleteById(Long id);

    List<Manga> findAllByTagId(Long tagId);
}


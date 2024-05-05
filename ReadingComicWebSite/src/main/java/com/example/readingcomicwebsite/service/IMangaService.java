package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Manga;

import java.util.List;

public interface IMangaService {
    List<Manga> findAll();

    Manga findById(Integer id);

    List<Manga> findByName(String name);

    Manga add(Manga manga);

    Manga update(Integer id, Manga manga);

    void deleteById(Integer id);

    List<Manga> findAllByTagId(Long tagId);
}


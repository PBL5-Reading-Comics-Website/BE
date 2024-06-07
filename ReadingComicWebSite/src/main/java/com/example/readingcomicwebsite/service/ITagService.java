package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Tag;

import java.util.List;

public interface ITagService {
    List<Tag> findAll();

    Tag findById(Integer id);
    
    Tag findByName(String name);

    Tag add(Tag tag);

    Tag update(Integer id, Tag tag);

    void deleteById(Integer id);

    List<Tag> findAllByMangaId(Integer mangaId);
}

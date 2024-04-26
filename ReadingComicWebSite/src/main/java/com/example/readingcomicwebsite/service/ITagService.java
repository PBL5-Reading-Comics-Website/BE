package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Tag;

import java.util.List;

public interface ITagService {
    List<Tag> findAll();

    Tag findById(Long id);

    Tag add(Tag tag);

    Tag update(Long id, Tag tag);

    void deleteById(Long id);

    List<Tag> findAllByMangaId(Long mangaId);
}

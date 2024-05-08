package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.Tag;
import com.example.readingcomicwebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMangaService {
    Page<Manga> findAll(String sortField, String sortOrder, Integer page, Integer size);

    Manga findById(Integer id);

    List<Manga> findByName(String name);

    Manga add(Manga manga, User user);

    Manga update(Integer id, Manga manga);

    void deleteById(Integer id);

    Page<Manga> findAllByTagId(Integer tagId, String sortField, String sortOrder, Integer page, Integer size);

    Manga likeManga(Integer id);

    Manga viewManga(Integer id);

    List<Manga> getTop10NewestManga();

    Page<Manga> getMangaPublishedInFirstQuarter(Integer page, Integer size, String sortField, String sortOrder);

    Page<Manga> getMangaPublishedInSecondQuarter(Integer page, Integer size, String sortField, String sortOrder);

    Page<Manga> getMangaPublishedInThirdQuarter(Integer page, Integer size, String sortField, String sortOrder);

    Page<Manga> getMangaPublishedInFourthQuarter(Integer page, Integer size, String sortField, String sortOrder);

    Page<Manga> findByTagAndName(Integer tagId, String name, String sortField, String sortOrder, Integer page,
                                 Integer size);

    Tag addTag(Integer mangaId, Integer tagId);
}


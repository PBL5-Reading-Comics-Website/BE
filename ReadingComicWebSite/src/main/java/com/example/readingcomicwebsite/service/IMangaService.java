package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMangaService {
    Page<Manga> findAll(String sortField, String sortOrder, Integer page, Integer size);

    Manga findById(Integer id);

    Page<Manga> findByName(String name, Pageable pageable);

    Manga add(Manga manga, Integer userId);

    Manga update(Integer id, Manga manga);

    void deleteById(Integer id);

    Manga likeManga(Integer id, Integer userId);

    Manga dislikeManga(Integer id, Integer userId);

    Manga viewManga(Integer id);

    List<Manga> getTop10NewestManga();

    Page<Manga> getMangaPublishedInFirstQuarter(Integer page, Integer size, String sortField, String sortOrder);

    Page<Manga> getMangaPublishedInSecondQuarter(Integer page, Integer size, String sortField, String sortOrder);

    Page<Manga> getMangaPublishedInThirdQuarter(Integer page, Integer size, String sortField, String sortOrder);

    Page<Manga> getMangaPublishedInFourthQuarter(Integer page, Integer size, String sortField, String sortOrder);

    Page<Manga> findByTagAndName(String tag, String name, String sortField, String sortOrder, Integer page,
                                 Integer size);

    Tag addTag(Integer mangaId, Integer tagId);

    Object findByUserId(Integer userId);

    Boolean isLikedManga(Integer mangaId, Integer userId);
}


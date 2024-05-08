package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.dto.ChapterDto;
import com.example.readingcomicwebsite.model.Chapter;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IChapterService {
    Page<Chapter> findAll(String sortField, String sortOrder, Integer page, Integer size);

    Chapter findById(Integer id);

    Chapter add(Integer mangaId, ChapterDto chapterDto);

    Chapter update(Integer id, Chapter chapter);

    void deleteById(Integer id);

    List<Chapter> findAllByMangaId(Integer mangaId);

    boolean isChapterExist(Integer id);
}

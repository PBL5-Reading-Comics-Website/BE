package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.dto.ChapterDto;
import com.example.readingcomicwebsite.model.Chapter;
import com.example.readingcomicwebsite.model.Image;
import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.repository.ChapterRepository;
import com.example.readingcomicwebsite.service.IChapterService;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChapterService implements IChapterService {
    private final ChapterRepository repository;
    private final MangaService mangaService;
    private final ImageService imageService;

    @Override
    public List<Chapter> findAll() {
        return repository.findAll();
    }

    @Override
    public Chapter findById(Integer id) {
        return repository.findChapterById(id).orElse(null);
    }

    @Override
    public Chapter add(Integer mangaId, ChapterDto chapterDto) {
        Manga manga = mangaService.findById(mangaId);
        if (manga == null) {
            return null;
        }
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterDto, chapter);
        chapter.setManga(manga);
        chapter.setCommentNumber(0);
        chapter.setPublishAt(Instant.now());
        chapter.setUpdateAt(chapter.getUpdateAt() == null ? chapter.getPublishAt() : chapter.getUpdateAt());
        return repository.save(chapter);
    }

    @Override
    public Chapter update(Integer id, Chapter chapter) {
        Chapter chapterDb = repository.findChapterById(id).orElse(null);
        if (chapterDb == null) {
            return null;
        }
        BeanUtils.copyProperties(chapter, chapterDb);
        return repository.save(chapterDb);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteChapterById(id);
    }

    @Override
    public List<Chapter> findAllByMangaId(Integer mangaId) {
        return repository.findAllByMangaId(mangaId);
    }

    @Override
    public boolean isChapterExist(Integer id) {
        return repository.existsChapterById(id);
    }
}

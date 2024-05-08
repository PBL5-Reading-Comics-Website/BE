package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Chapter;
import com.example.readingcomicwebsite.repository.ChapterRepository;
import com.example.readingcomicwebsite.service.IChapterService;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChapterService implements IChapterService {
    private final ChapterRepository repository;

    @Override
    public Page<Chapter> findAll(String sortField, String sortOrder, Integer page, Integer size) {
        return repository.findAll(PageUtils.makePageRequest(sortField, sortOrder, page, size));
    }

    @Override
    public Chapter findById(Integer id) {
        return repository.findChapterById(id).orElse(null);
    }

    @Override
    public Chapter add(Chapter chapter) {
        if (chapter.getData() == null || chapter.getData().isEmpty()) {
            return null;
        } else if (chapter.getManga() == null) {
            return null;
        } else if (chapter.getName() == null || chapter.getName().isEmpty()) {
            return null;
        } else if (chapter.getNumber() == null) {
            return null;
        }
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

package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Chapter;
import com.example.readingcomicwebsite.repository.ChapterRepository;
import com.example.readingcomicwebsite.service.IChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChapterService implements IChapterService {
    private final ChapterRepository repository;

    @Override
    public List<Chapter> findAll() {
        return repository.findAll();
    }

    @Override
    public Chapter findById(Long id) {
        return repository.findById(id).orElse(null);
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
    public Chapter update(Long id, Chapter chapter) {
        Chapter chapterDb = repository.findById(id).orElse(null);
        if (chapterDb == null) {
            return null;
        }
        BeanUtils.copyProperties(chapter, chapterDb);
        return repository.save(chapterDb);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Chapter> findAllByMangaId(Long mangaId) {
        return repository.findAllByMangaId(mangaId);
    }

    @Override
    public boolean isChapterExist(Long id) {
        return repository.existsById(id);
    }
}

package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Tag;
import com.example.readingcomicwebsite.repository.TagRepository;
import com.example.readingcomicwebsite.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService implements ITagService {
    private final TagRepository repository;

    @Override
    public List<Tag> findAll() {
        return repository.findAllNoDuplicate();
    }

    @Override
    public Tag findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Tag add(Tag tag) {
        if (tag.getName() == null || tag.getName().isEmpty())
            return null;
        return repository.save(tag);
    }

    @Override
    public Tag update(Integer id, Tag tag) {
        Tag tagDb = repository.findById(id).orElse(null);
        if (tagDb == null)
            return null;
        BeanUtils.copyProperties(tag, tagDb);
        return repository.save(tagDb);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Tag> findAllByMangaId(Integer mangaId) {
        return repository.findAllByMangaId(mangaId);
    }
}

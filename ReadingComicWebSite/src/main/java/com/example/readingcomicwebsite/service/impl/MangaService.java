package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.repository.MangaRepository;
import com.example.readingcomicwebsite.service.IMangaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MangaService implements IMangaService {
    private final MangaRepository repository;

    @Override
    public List<Manga> findAll() {
        return repository.findAll();
    }

    @Override
    public Manga findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Manga> findByName(String name) {
        return repository.findAllByNameLike(name);
    }

    @Override
    public Manga add(Manga manga) {
        if (manga.getName() == null || manga.getName().isEmpty()) {
            return null;
        } else if (manga.getAuthor() == null || manga.getAuthor().isEmpty()) {
            return null;
        } else if (manga.getArtist() == null || manga.getArtist().isEmpty()) {
            return null;
        }
        return repository.save(manga);
    }

    @Override
    public Manga update(Integer id, Manga manga) {
        Manga mangaDb = repository.findById(id).orElse(null);
        if (mangaDb == null) {
            return null;
        }
        BeanUtils.copyProperties(manga, mangaDb);
        return repository.save(mangaDb);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Manga> findAllByTagId(Long tagId) {
        return repository.findAllByTagId(tagId);
    }
}

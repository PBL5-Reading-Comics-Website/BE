package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.auth.ErrorResponse;
import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.repository.MangaRepository;
import com.example.readingcomicwebsite.service.IMangaService;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MangaService implements IMangaService {
    private final MangaRepository repository;

    @Override
    public Page<Manga> findAll(String sortField, String sortOrder, Integer page, Integer size) {
        return repository.findAll(PageUtils.makePageRequest(sortField, sortOrder, page, size));
    }

    @Override
    @Transactional
    public Manga findById(Integer id) {
        Manga manga = repository.findById(id)
                .orElseThrow(() -> new Error("Manga not found with id " + id));
        manga.setViewNumber(manga.getViewNumber() + 1);
        return repository.save(manga);
    }

    @Override
    public List<Manga> findByName(String name) {
        return repository.findAllByNameLike(name);
    }

    @Override
    @Transactional
    public Manga add(Manga manga, User user) {
        if (manga.getName() == null || manga.getName().isEmpty()) {
            return null;
        } else if (manga.getAuthor() == null || manga.getAuthor().isEmpty()) {
            return null;
        } else if (manga.getArtist() == null || manga.getArtist().isEmpty()) {
            return null;
        }
        manga.setUpdateUser(user.getId());
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

    @Override
    public Manga likeManga(Integer id) {
        Manga manga = repository.findById(id).orElse(null);
        if (manga == null) {
            return null;
        }
        manga.setFavouriteNumber(manga.getFavouriteNumber() + 1);
        return repository.save(manga);
    }

    @Override
    public Manga viewManga(Integer id) {
        Manga manga = repository.findById(id).orElse(null);
        if (manga == null) {
            return null;
        }
        manga.setFavouriteNumber(manga.getFavouriteNumber() + 1);
        return repository.save(manga);
    }

    @Override
    public List<Manga> getTop10NewestManga() {
        return repository.findTop10ByOrderByPublishAtDesc();
    }

    @Override
    public Page<Manga> getMangaPublishedInFirstQuarter(Integer page, Integer size, String sortField, String sortOrder) {
        return repository.findAllPublishedInFirstQuarter(PageUtils.makePageRequest(sortField, sortOrder, page, size));
    }

    @Override
    public Page<Manga> getMangaPublishedInSecondQuarter(Integer page, Integer size, String sortField, String sortOrder) {
        return repository.findAllPublishedInSecondQuarter(PageUtils.makePageRequest(sortField, sortOrder, page, size));
    }

    @Override
    public Page<Manga> getMangaPublishedInThirdQuarter(Integer page, Integer size, String sortField, String sortOrder) {
        return repository.findAllPublishedInThirdQuarter(PageUtils.makePageRequest(sortField, sortOrder, page, size));
    }

    @Override
    public Page<Manga> getMangaPublishedInFourthQuarter(Integer page, Integer size, String sortField, String sortOrder) {
        return repository.findAllPublishedInFourthQuarter(PageUtils.makePageRequest(sortField, sortOrder, page, size));
    }
}

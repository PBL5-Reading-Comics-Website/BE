package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.Tag;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.repository.MangaRepository;
import com.example.readingcomicwebsite.repository.TagRepository;
import com.example.readingcomicwebsite.repository.UserRepository;
import com.example.readingcomicwebsite.service.IMangaService;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MangaService implements IMangaService {
    private final MangaRepository repository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

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
    public Page<Manga> findByName(String name, Pageable pageable) {
        return repository.findAllByNameLike(name, pageable);
    }

    @Override
    @Transactional
    public Manga add(Manga manga, Integer userId) {
        if (manga.getName() == null || manga.getName().isEmpty()) {
            return null;
        } else if (manga.getAuthor() == null || manga.getAuthor().isEmpty()) {
            return null;
        } else if (manga.getArtist() == null || manga.getArtist().isEmpty()) {
            return null;
        }
        System.out.print("Manga tags: ");
        for (Tag tag : manga.getTags()) {
            System.out.print(tag.getName() + ", ");
        }
        System.out.println("");
        System.out.println("Name: " + manga.getName());
        manga.setUpdateUser(userId);
        manga.setPublishAt(Instant.now());
        manga.setUpdateAt(manga.getUpdateAt() == null ? manga.getPublishAt() : manga.getUpdateAt());
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
    public Manga likeManga(Integer id, Integer userId) {
        Manga mangaDb = repository.findById(id).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (mangaDb == null || user == null) {
            return null;
        }
        for (Manga manga : user.getLikedManga()) {
            if (manga == mangaDb) {
                throw new Error("Manga already liked");
            }
        }
        user.getLikedManga().add(mangaDb);
        mangaDb.setFavouriteNumber(mangaDb.getFavouriteNumber() + 1);
        return repository.save(mangaDb);
    }

    @Override
    public Manga dislikeManga(Integer id, Integer userId) {
        Manga mangaDb = repository.findById(id).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (mangaDb == null || user == null) {
            return null;
        }
        if (isLikedManga(id, userId)) {
            user.getLikedManga().remove(mangaDb);
            mangaDb.setFavouriteNumber(mangaDb.getFavouriteNumber() - 1);
            return repository.save(mangaDb);
        }
        throw new Error("Manga not liked");
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

    @Override
    public Page<Manga> findByTagAndName(String tag, String name, String sortField, String sortOrder, Integer page,
                                        Integer size) {
        if (tag == null && name == null) {
            return repository.findAll(PageUtils.makePageRequest(sortField, sortOrder, page, size));
        } else if (tag == null) {
            return repository.findAllByNameLike(
                    "%" + name + "%",
                    PageUtils.makePageRequest(sortField, sortOrder, page, size));
        } else if (name == null) {
//            return repository.findAllByTagId(
//                    Integer.parseInt(tag),
//                    PageUtils.makePageRequest(sortField, sortOrder, page, size));
            return null;
        }
        return repository.findAllByTagIdAndName(
                tag,
                "%" + name + "%",
                PageUtils.makePageRequest(sortField, sortOrder, page, size));
    }

    @Override
    @Transactional
    public Tag addTag(Integer mangaId, Integer tagId) {
        Manga mangaDb = repository.findById(mangaId).orElse(null);
        Tag tagDb = tagRepository.findById(tagId).orElse(null);

        if (mangaDb == null || tagDb == null) {
            return null;
        }

        // Add the tag to the manga's tags collection
        mangaDb.getTags().add(tagDb);

        // Save the Manga to update the relationship in the database
        repository.save(mangaDb);

        // Return the added Tag (not the Manga)
        return tagDb;
    }

    @Override
    public List<Manga> findByUserId(Integer userId) {
        return repository.findAllByUpdateUser(userId);
    }

    @Override
    public Boolean isLikedManga(Integer mangaId, Integer userId) {
        Manga mangaDb = repository.findById(mangaId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (mangaDb == null || user == null) {
            return null;
        }
        if (user.getLikedManga().contains(mangaDb)) {
            return true;
        }
        return false;
    }
}

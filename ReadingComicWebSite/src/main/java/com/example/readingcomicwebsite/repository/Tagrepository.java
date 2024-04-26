package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tagrepository extends JpaRepository<Tag, Long> {
    //find all tag by manga id
    @Query(value = "SELECT t.* FROM tag t JOIN manga m ON t.manga_id = m.id WHERE m.id = :mangaId;", nativeQuery = true)
    List<Tag> findAllByMangaId(@Param("mangaId") Long mangaId);

    List<Tag> findAllByNameLike(String name);
}

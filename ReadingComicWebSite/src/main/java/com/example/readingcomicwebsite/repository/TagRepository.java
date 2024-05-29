package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(value = "SELECT t.* " +
            "FROM manga_tag mt " +
            "JOIN tag t ON mt.tag_id = t.id " +
            "WHERE mt.manga_id = :mangaId", nativeQuery = true)
    List<Tag> findAllByMangaId(@Param("mangaId") Integer mangaId);

    List<Tag> findAllByNameLike(String name);

    //find all tag, no duplicate
    @Query(value = "SELECT DISTINCT tag.* FROM tag", nativeQuery = true)
    List<Tag> findAllNoDuplicate();
}

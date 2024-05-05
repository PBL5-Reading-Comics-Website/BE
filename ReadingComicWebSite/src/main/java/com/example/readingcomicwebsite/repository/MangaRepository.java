package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Integer> {
    //find all manga by tag id
     @Query(value = "SELECT m.* FROM manga m JOIN tag t ON m.id = t.manga_id WHERE t.id = :tagId;", nativeQuery = true)
     List<Manga> findAllByTagId(@Param("tagId") Long tagId);

     List<Manga> findAllByNameLike(String name);
}

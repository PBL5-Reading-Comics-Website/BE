package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Manga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<Manga> findTop10ByOrderByPublishAtDesc();

    @Query("SELECT m FROM Manga m WHERE MONTH(m.publishAt) IN (1, 2, 3)")
    Page<Manga> findAllPublishedInFirstQuarter(Pageable pageable);

    @Query("SELECT m FROM Manga m WHERE MONTH(m.publishAt) IN (4, 5, 6)")
    Page<Manga> findAllPublishedInSecondQuarter(Pageable pageable);

    @Query("SELECT m FROM Manga m WHERE MONTH(m.publishAt) IN (7, 8, 9)")
    Page<Manga> findAllPublishedInThirdQuarter(Pageable pageable);

    @Query("SELECT m FROM Manga m WHERE MONTH(m.publishAt) IN (10, 11, 12)")
    Page<Manga> findAllPublishedInFourthQuarter(Pageable pageable);
}

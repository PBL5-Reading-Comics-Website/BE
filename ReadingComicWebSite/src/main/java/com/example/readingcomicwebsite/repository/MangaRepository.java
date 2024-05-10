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
    @Query(value = "SELECT manga.* FROM manga INNER JOIN tag ON manga.id = tag.manga_id WHERE tag.id = :tagId",
            nativeQuery = true)
    Page<Manga> findAllByTagId(@Param("tagId") Integer tagId, Pageable pageable);

    Page<Manga> findAllByNameLike(String name, Pageable pageable);

    List<Manga> findTop10ByOrderByPublishAtDesc();

    @Query("SELECT m FROM Manga m WHERE MONTH(m.publishAt) IN (1, 2, 3)")
    Page<Manga> findAllPublishedInFirstQuarter(Pageable pageable);

    @Query("SELECT m FROM Manga m WHERE MONTH(m.publishAt) IN (4, 5, 6)")
    Page<Manga> findAllPublishedInSecondQuarter(Pageable pageable);

    @Query("SELECT m FROM Manga m WHERE MONTH(m.publishAt) IN (7, 8, 9)")
    Page<Manga> findAllPublishedInThirdQuarter(Pageable pageable);

    @Query("SELECT m FROM Manga m WHERE MONTH(m.publishAt) IN (10, 11, 12)")
    Page<Manga> findAllPublishedInFourthQuarter(Pageable pageable);

    @Query(value = "SELECT distinct manga.* FROM manga, tag WHERE manga.name LIKE :name OR tag.name = :tag", nativeQuery = true)
    Page<Manga> findAllByTagIdAndName(@Param("tag") String tag, @Param("name") String name, Pageable pageable);

    List<Manga> findAllByUpdateUser(Integer userId);
}

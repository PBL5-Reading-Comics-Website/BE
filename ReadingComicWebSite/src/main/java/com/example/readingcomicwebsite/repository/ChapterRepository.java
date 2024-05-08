package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    @Query(value = "SELECT DATE(publish_at) as publish_date FROM :chapter", nativeQuery = true)
    Date getPublishDate(@Param("chapter") Chapter chapter);

    // find all chapter by manga id
    @Query(value = "SELECT * FROM chapter WHERE manga_id = :mangaId", nativeQuery = true)
    List<Chapter> findAllByMangaId(@Param("mangaId") Integer mangaId);

    Optional<Chapter> findChapterById(Integer id);

    void deleteChapterById(Integer id);

    boolean existsChapterById(Integer id);
}

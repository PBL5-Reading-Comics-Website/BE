package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    //find all tag by manga id
    @Query(value = "SELECT tag.* FROM tag WHERE tag.manga_id = :mangaId", nativeQuery = true)
    List<Tag> findAllByMangaId(@Param("mangaId") Integer mangaId);

    List<Tag> findAllByNameLike(String name);
}

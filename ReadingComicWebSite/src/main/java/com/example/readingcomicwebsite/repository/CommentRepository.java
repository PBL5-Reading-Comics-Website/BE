package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "SELECT DATE(create_at) as create_at FROM :comment;", nativeQuery = true)
    Date getCreateDate(@Param("comment") Comment comment);

    // find all comment by manga id
    @Query(value = "SELECT * FROM comment WHERE manga_id = :mangaId;", nativeQuery = true)
    List<Comment> findByMangaId(Long mangaId);
}

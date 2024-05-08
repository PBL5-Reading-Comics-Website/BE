package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.model.Comment;

import java.util.List;

public interface ICommentService {
    List<Comment> findAll();

    List<Comment> findAllByMangaId(Integer mangaId);

    Comment save(Comment comment);

    Comment update(Integer id, Comment comment);

    Comment replyComment(Integer id, Integer mangaId, Integer userId, Comment comment);

    void deleteById(Integer id);

    Comment findById(Integer id);

    Comment add(Integer mangaId, Integer userId, Comment comment);

    List<Comment> findAllReplyCommentsByCommentId(Integer id);
}

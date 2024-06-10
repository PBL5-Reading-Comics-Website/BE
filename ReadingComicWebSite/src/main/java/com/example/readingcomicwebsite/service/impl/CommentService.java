package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.repository.CommentRepository;
import com.example.readingcomicwebsite.repository.MangaRepository;
import com.example.readingcomicwebsite.repository.UserRepository;
import com.example.readingcomicwebsite.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository repository;
    private final MangaRepository mangaRepository;
    private final UserRepository userRepository;

    @Override
    public List<Comment> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Comment> findAllByMangaId(Integer mangaId) {
        return repository.findByMangaId(mangaId);
    }

    @Override
    public Comment save(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public Comment update(Integer id, Comment comment) {
        Comment commentDb = repository.findById(id).orElse(null);
        if (commentDb == null) {
            return null;
        }
        BeanUtils.copyProperties(comment, commentDb);
        return repository.save(commentDb);
    }

    @Override
    public Comment replyComment(Integer id, Integer mangaId, Integer userId, Comment comment) {
        Comment commentDb = repository.findById(id).orElse(null);
        Manga manga = mangaRepository.findById(mangaId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (commentDb == null || manga == null || user == null) {
            return null;
        }
        comment.setManga(manga);
        comment.setUser(user);
        comment.setReplyId(commentDb.getId());
        return repository.save(comment);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Comment findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Comment add(Integer mangaId, Integer userId, Comment comment) {
        Manga manga = mangaRepository.findById(mangaId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (manga == null || user == null) {
            return null;
        }
        manga.setCommentNumber(manga.getCommentNumber() + 1);
        comment.setUser(user);
        comment.setCreateAt(Instant.now());
        comment.setUpdateAt(comment.getUpdateAt() == null ? comment.getCreateAt() : comment.getUpdateAt());
        comment.setManga(manga);
        return repository.save(comment);
    }

    @Override
    public List<Comment> findAllReplyCommentsByCommentId(Integer id) {
        return repository.findByReplyId(id);
    }
}

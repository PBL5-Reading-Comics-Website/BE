package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.repository.CommentRepository;
import com.example.readingcomicwebsite.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository repository;

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
    public Comment replyComment(Integer id, Comment comment) {
        //write the function to reply the existing comment
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Comment findById(Integer id) {
        return repository.findById(id).orElse(null);
    }
}

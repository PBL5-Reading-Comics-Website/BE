package com.example.readingcomicwebsite.service.impl;

import com.example.readingcomicwebsite.model.Chapter;
import com.example.readingcomicwebsite.model.Image;
import com.example.readingcomicwebsite.repository.ImageRepository;
import com.example.readingcomicwebsite.service.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository repository;

    @Override
    public List<Image> list(Chapter chapter) {
        return repository.findAllByChapterOrderById(chapter);
    }

    @Override
    public Optional<Image> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public void save(Image image) {
        repository.save(image);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean isExist(Long id) {
        return repository.existsById(id);
    }
}

package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.auth.ApiDataResponse;
import com.example.readingcomicwebsite.dto.ChapterDto;
import com.example.readingcomicwebsite.dto.MangaDto;
import com.example.readingcomicwebsite.model.*;
import com.example.readingcomicwebsite.service.*;
import com.example.readingcomicwebsite.service.impl.ImageService;
import com.example.readingcomicwebsite.service.impl.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/poster")
@RequiredArgsConstructor
@CrossOrigin()
public class PosterController {
    private final IMangaService mangaService;
    private final IChapterService chapterService;
    private final ImageService imageService;
    private final TagService tagService;

    // add tag for manga
    @PostMapping("/tag")
    public ResponseEntity<ApiDataResponse> addTag(
            @RequestParam Integer mangaId,
            @RequestParam Integer tagId
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(mangaService.addTag(mangaId, tagId)));
    }

    // create new chapter with manga id
    @PostMapping("/chapter")
    public ResponseEntity<ApiDataResponse> createChapter(
            @RequestBody ChapterDto chapterDto,
            @RequestParam Integer mangaId
    ) {
        Chapter chapter = chapterService.add(mangaId, chapterDto);
        List<String> images = chapterDto.getImages();
        if (images == null || images.isEmpty()) {
            return ResponseEntity.ok(ApiDataResponse.error("Images is required"));
        }
        for (String imageName : images) {
            String[] parts = imageName.split("/");
            String imageLink = parts[parts.length - 1].split("\\.")[0];
            Image image = new Image(imageLink, chapter);
            imageService.add(image);
        }
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(chapter));
    }

    // Endpoint for updating a chapter by id
    @PutMapping("/chapter/{id}")
    public ResponseEntity<ApiDataResponse> updateChapterById(
            @PathVariable Integer id,
            @RequestBody Chapter chapter
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(chapterService.update(id, chapter)));
    }

    // Endpoint for deleting a chapter by id
    @DeleteMapping("/chapter/{id}")
    public ResponseEntity<ApiDataResponse> deleteChapterById(@PathVariable Integer id) {
        chapterService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @GetMapping("/manga/{name}")
    public ResponseEntity<ApiDataResponse> getMangaByName(
            @PathVariable(value = "name", required = false) String name
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(mangaService.findByName(name, null)));
    }

    @PostMapping("/manga")
    public ResponseEntity<ApiDataResponse> createManga(
            @RequestBody MangaDto mangaDto,
            @RequestParam Integer userId
    ) {
        Manga manga = new Manga();
        BeanUtils.copyProperties(mangaDto, manga);
        List<Tag> tags = new ArrayList<>();

        // Convert string array to a list of Tag objects
        for (String tagName : mangaDto.getTags()) {
            Tag tag = tagService.findByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tagService.add(tag);
            } else {
                tag = tagService.findByName(tagName);
            }
            tags.add(tag);
        }

        // Set the tags in the Manga entity
        manga.setTags(new HashSet<>(tags));

        System.out.println(manga);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(mangaService.add(manga, userId)));
    }

    @PutMapping("/manga/{id}")
    public ResponseEntity<ApiDataResponse> updateManga(
            @PathVariable Integer id,
            @RequestBody MangaDto mangaDto
    ) {
        Manga manga = new Manga();
        BeanUtils.copyProperties(mangaDto, manga);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(mangaService.update(id, manga)));
    }

    @DeleteMapping("/manga/{id}")
    public ResponseEntity<ApiDataResponse> deleteManga(
            @PathVariable Integer id
    ) {
        mangaService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    // get all manga you have created
    @GetMapping("/manga")
    public ResponseEntity<ApiDataResponse> getMangaByUser(
            @RequestParam Integer userId
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(mangaService.findByUserId(userId)));
    }
}

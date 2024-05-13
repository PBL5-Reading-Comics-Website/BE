package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.auth.ApiDataResponse;
import com.example.readingcomicwebsite.dto.ChapterDto;
import com.example.readingcomicwebsite.dto.MangaDto;
import com.example.readingcomicwebsite.model.*;
import com.example.readingcomicwebsite.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/poster")
@RequiredArgsConstructor
@CrossOrigin()
public class PosterController {
    private final IMangaService mangaService;
    private final IChapterService chapterService;

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

        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(chapterService.add(mangaId, chapterDto)));
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

    @PostMapping("/manga")
    public ResponseEntity<ApiDataResponse> createManga(
            @RequestBody MangaDto mangaDto,
            @RequestParam Integer userId
    ) {
        Manga manga = new Manga();
        BeanUtils.copyProperties(mangaDto, manga);
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

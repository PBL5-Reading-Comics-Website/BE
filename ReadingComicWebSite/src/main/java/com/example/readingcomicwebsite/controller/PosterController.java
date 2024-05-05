package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.dto.MangaDto;
import com.example.readingcomicwebsite.model.Manga;
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
    private final IUserService userService;
    private final IMangaService mangaService;
    private final IChapterService chapterService;
    private final ICommentService commentService;
    private final IReportService reportService;
    private final ITagService tagService;
    private final IFollowingService followingService;
    private final IReadingHistoryService readingHistoryService;

    @PostMapping("manga-new")
    public ResponseEntity<?> createManga(
            @RequestBody MangaDto mangaDto
    ) {
        Manga manga = new Manga();
        BeanUtils.copyProperties(mangaDto, manga);
        return ResponseEntity.ok(mangaService.add(manga));
    }

    @PutMapping("manga-update/{id}")
    public ResponseEntity<?> updateManga(
            @PathVariable Integer id,
            @RequestBody MangaDto mangaDto
    ) {
        Manga manga = new Manga();
        BeanUtils.copyProperties(mangaDto, manga);
        return ResponseEntity.ok(mangaService.update(id, manga));
    }

//    @DeleteMapping("manga-delete/{id}")
//    public ResponseEntity<?> deleteManga(
//            @PathVariable Integer id
//    ) {
//        return ResponseEntity.ok(mangaService.deleteById(id));
//    }
}

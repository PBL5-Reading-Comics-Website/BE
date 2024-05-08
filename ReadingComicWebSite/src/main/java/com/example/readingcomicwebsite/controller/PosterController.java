package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.dto.MangaDto;
import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.User;
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
            @RequestBody MangaDto mangaDto,
            @RequestBody User user
    ) {
        Manga manga = new Manga();
        BeanUtils.copyProperties(mangaDto, manga);
        return ResponseEntity.ok(mangaService.add(manga, user));
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

    // Endpoint for deleting a manga by id
    @DeleteMapping("/manga/{id}")
    public void deleteMangaById(@PathVariable Integer id) {
        mangaService.deleteById(id);
    }

    // Endpoint for deleting a chapter by id
    @DeleteMapping("/chapter/{id}")
    public void deleteChapterById(@PathVariable Integer id) {
        chapterService.deleteById(id);
    }

    @PostMapping("/comment")
    public Comment addComment(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

    @PutMapping("/comment/{id}")
    public Comment updateComment(@PathVariable Integer id, @RequestBody Comment comment) {
        return commentService.update(id, comment);
    }

    // Endpoint for deleting a comment by id
    @DeleteMapping("/comment/{id}")
    public void deleteCommentById(@PathVariable Integer id) {
        commentService.deleteById(id);
    }

    @DeleteMapping("manga-delete/{id}")
    public ResponseEntity deleteManga(
            @PathVariable Integer id
    ) {
        mangaService.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}

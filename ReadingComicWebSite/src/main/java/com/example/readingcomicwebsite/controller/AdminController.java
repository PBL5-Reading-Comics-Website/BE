package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.auth.ApiDataResponse;
import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Report;
import com.example.readingcomicwebsite.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin()
public class AdminController {
    private final IUserService userService;
    private final IMangaService mangaService;
    private final IChapterService chapterService;
    private final ICommentService commentService;
    private final IReportService reportService;
    private final ITagService tagService;
    private final IFollowingService followingService;
    private final IReadingHistoryService readingHistoryService;

    // Endpoint for deleting a user by id
    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiDataResponse> deleteUserById(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    // Endpoint for deleting a manga by id
    @DeleteMapping("/manga/{id}")
    public ResponseEntity<ApiDataResponse> deleteMangaById(@PathVariable Integer id) {
        mangaService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    // Endpoint for deleting a chapter by id
    @DeleteMapping("/chapter/{id}")
    public ResponseEntity<ApiDataResponse> deleteChapterById(@PathVariable Integer id) {
        chapterService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @PostMapping("/comment")
    public ResponseEntity<ApiDataResponse> addComment(@RequestParam Integer mangaId, @RequestParam Integer userId,
                                                      @RequestBody Comment comment) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(commentService.add(mangaId, userId, comment)));
    }

    @PostMapping("/comment-reply/{id}")
    public ResponseEntity<ApiDataResponse> replyComment(
            @PathVariable Integer id,
            @RequestParam Integer mangaId,
            @RequestParam Integer userId,
            @RequestBody Comment comment) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(commentService.replyComment(id, mangaId, userId,
                comment)));
    }

    // Endpoint for deleting a comment by id
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<ApiDataResponse> deleteCommentById(@PathVariable Integer id) {
        commentService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    // Endpoint for deleting a tag by id
    @DeleteMapping("/tag/{id}")
    public void deleteTagById(@PathVariable Integer id) {
        tagService.deleteById(id);
    }


    // Endpoint for getting all reports
    @GetMapping("/reports")
    public ResponseEntity<ApiDataResponse> getAllReports() {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(reportService.findAll()));
    }

    // Endpoint for getting a report by id
    @GetMapping("/report/{id}")
    public ResponseEntity<ApiDataResponse> getReportById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(reportService.findById(id)));
    }

    // Endpoint for deleting a report by id
    @DeleteMapping("/report/{id}")
    public ResponseEntity<ApiDataResponse> deleteReportById(@PathVariable Integer id) {
        reportService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    // find all comments of manga by mangaId
    @GetMapping("/comments/{mangaId}")
    public ResponseEntity<ApiDataResponse> findAllCommentsByMangaId(@PathVariable Integer mangaId) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(commentService.findAllByMangaId(mangaId)));
    }

    // find all reply comments by comment id
    @GetMapping("/comments/reply/{id}")
    public ResponseEntity<ApiDataResponse> findAllReplyCommentsByCommentId(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(commentService.findAllReplyCommentsByCommentId(id)));
    }
}
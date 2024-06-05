package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.auth.ApiDataResponse;
import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.Report;
import com.example.readingcomicwebsite.model.Waiting;
import com.example.readingcomicwebsite.service.*;
import com.example.readingcomicwebsite.service.impl.WaitingService;
import com.example.readingcomicwebsite.util.PageInfo;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final WaitingService waitingService;

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
    public ResponseEntity<ApiDataResponse> getAllReports(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Report> reportPage = reportService.findAll(PageUtils.makePageRequest(sortField, sortOrder, page, size));
        PageInfo pageInfo = PageUtils.makePageInfo(reportPage);
        return ResponseEntity.ok(ApiDataResponse.success(reportPage.getContent(), pageInfo));
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

    // get all waiting users
    @GetMapping("/waitings")
    public ResponseEntity<ApiDataResponse> getAllWaitings(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Waiting> waitingPage = waitingService.findAll(page, size);
        PageInfo pageInfo = PageUtils.makePageInfo(waitingPage);
        return ResponseEntity.ok(ApiDataResponse.success(waitingPage.getContent(), pageInfo));
    }

    // accept waiting user
    @PutMapping("/waiting/accept")
    public ResponseEntity<ApiDataResponse> acceptWaiting(@RequestParam Integer id) {
        waitingService.acceptWaiting(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    // reject waiting user
    @PutMapping("/waiting/reject")
    public ResponseEntity<ApiDataResponse> rejectWaiting(@RequestParam Integer id) {
        waitingService.rejectWaiting(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }
}
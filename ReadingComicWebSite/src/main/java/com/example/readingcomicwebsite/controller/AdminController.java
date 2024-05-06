package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Report;
import com.example.readingcomicwebsite.service.*;
import lombok.RequiredArgsConstructor;
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
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteById(id);
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
    // Endpoint for deleting a comment by id
    @DeleteMapping("/comment/{id}")
    public void deleteCommentById(@PathVariable Integer id) {
        commentService.deleteById(id);
    }

    // Endpoint for getting all reports
    @GetMapping("/reports")
    public List<Report> getAllReports() {
        return reportService.findAll();
    }

    // Endpoint for getting a report by id
    @GetMapping("/report/{id}")
    public Report getReportById(@PathVariable Integer id) {
        return reportService.findById(id);
    }
    // Endpoint for deleting a tag by id
    @DeleteMapping("/tag/{id}")
    public void deleteTagById(@PathVariable Integer id) {
        tagService.deleteById(id);
    }

}
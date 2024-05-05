package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin()
public class UserController {
    private final IUserService userService;
    private final IMangaService mangaService;
    private final IChapterService chapterService;
    private final ICommentService commentService;
    private final IReportService reportService;
    private final ITagService tagService;
    private final IFollowingService followingService;
    private final IReadingHistoryService readingHistoryService;

    @GetMapping("/followings")
    public Object getAllFollowings() {
        return followingService.findAll();
    }

    // Endpoint for getting a following by id
    @GetMapping("/following/{id}")
    public Object getFollowingById(Integer id) {
        return followingService.findById(id);
    }

    // Endpoint for deleting a following by id
    @DeleteMapping("/following/{id}")
    public void deleteFollowingById(Integer id) {
        followingService.deleteById(id);
    }

    // Endpoint for getting all reading histories
    @GetMapping("/reading-histories")
    public Object getAllReadingHistories() {
        return readingHistoryService.findAll();
    }

    // Endpoint for getting a reading history by id
    @GetMapping("/reading-history/{id}")
    public Object getReadingHistoryById(Integer id) {
        return readingHistoryService.findById(id);
    }

    // Endpoint for deleting a reading history by id
    @DeleteMapping("/reading-history/{id}")
    public void deleteReadingHistoryById(Integer id) {
        readingHistoryService.deleteById(id);
    }

}

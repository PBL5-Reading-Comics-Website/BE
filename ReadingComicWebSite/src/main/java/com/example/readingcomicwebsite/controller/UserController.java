package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.model.Following;
import com.example.readingcomicwebsite.model.ReadingHistory;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Following> getAllFollowings() {
        return followingService.findAll();
    }

    // Endpoint for getting a following by id
    @GetMapping("/following/{id}")
    public Following getFollowingById(Integer id) {
        return followingService.findById(id);
    }

    // Endpoint for deleting a following by id
    @DeleteMapping("/following/{id}")
    public void deleteFollowingById(Integer id) {
        followingService.deleteById(id);
    }

    // Endpoint for getting all reading histories
    @GetMapping("/reading-histories")
    public List<ReadingHistory> getAllReadingHistories() {
        return readingHistoryService.findAll();
    }

    // Endpoint for getting a reading history by id
    @GetMapping("/reading-history/{id}")
    public ReadingHistory getReadingHistoryById(Integer id) {
        return readingHistoryService.findById(id);
    }

    // Endpoint for deleting a reading history by id
    @DeleteMapping("/reading-history/{id}")
    public void deleteReadingHistoryById(Integer id) {
        readingHistoryService.deleteById(id);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        return userService.update(id, user);
    }

    // Endpoint for getting all comments
    @GetMapping("/comments")
    public Object getAllComments() {
        return commentService.findAll();
    }

    // Endpoint for getting a comment by id
    @GetMapping("/comment/{id}")
    public Object getCommentById(@PathVariable Integer id) {
        return commentService.findById(id);
    }

    // Endpoint for deleting a comment by id
    @DeleteMapping("/comment/{id}")
    public void deleteCommentById(@PathVariable Integer id) {
        commentService.deleteById(id);
    }

    // Endpoint for getting all mangas
    @GetMapping("/mangas")
    public Object getAllMangas() {
        return mangaService.findAll();
    }

    // Endpoint for getting a manga by id
    @GetMapping("/manga/{id}")
    public Object getMangaById(@PathVariable Integer id) {
        return mangaService.findById(id);
    }

    @PutMapping("/like-manga/{id}")
    public Object likeManga(@PathVariable Integer id) {
        return mangaService.likeManga(id);
    }
}

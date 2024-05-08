package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.auth.ApiDataResponse;
import com.example.readingcomicwebsite.model.Following;
import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.service.*;
import com.example.readingcomicwebsite.util.PageInfo;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<ApiDataResponse> getAllFollowings(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Following> followings = followingService.findAll(sortField, sortOrder, page, size);
        PageInfo pageInfo = PageUtils.makePageInfo(followings);
        return ResponseEntity.ok(ApiDataResponse.success(followings.getContent(), pageInfo));
    }

    // Endpoint for getting a following by id
    @GetMapping("/following/{id}")
    public ResponseEntity<ApiDataResponse> getFollowingById(@PathVariable Integer id) {
        Following following = followingService.findById(id);
        if (following == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(following));
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

    @GetMapping("/user/{id}")
    public Object getUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
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

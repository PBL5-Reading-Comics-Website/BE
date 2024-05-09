package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.auth.ApiDataResponse;
import com.example.readingcomicwebsite.model.*;
import com.example.readingcomicwebsite.service.*;
import com.example.readingcomicwebsite.util.PageInfo;
import com.example.readingcomicwebsite.util.PageUtils;
import com.example.readingcomicwebsite.util.StatusUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    // Endpoint for getting all followings by user id
    @GetMapping("/followings/{userId}")
    public ResponseEntity<ApiDataResponse> getFollowingsByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Following> followings = followingService.findByUserId(userId, sortField, sortOrder, page, size);
        PageInfo pageInfo = PageUtils.makePageInfo(followings);
        return ResponseEntity.ok(ApiDataResponse.success(followings.getContent(), pageInfo));
    }

    // following: {userId, mangaId}
    @PostMapping("/following")
    public ResponseEntity<ApiDataResponse> addFollowing(@RequestParam Integer userId, @RequestParam Integer mangaId) {
        User user = userService.findById(userId);
        Manga manga = mangaService.findById(mangaId);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(followingService.add(new Following(user, manga))));
    }

    // Endpoint for deleting a following by id
    @DeleteMapping("/following/{id}")
    public ResponseEntity<ApiDataResponse> deleteFollowingById(Integer id) {
        followingService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    // Endpoint for getting all reading histories
    @GetMapping("/reading-histories")
    public ResponseEntity<ApiDataResponse> getAllReadingHistories() {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(readingHistoryService.findAll()));
    }

    // Endpoint for getting a reading history by user id
    @GetMapping("/reading-histories/{userId}")
    public ResponseEntity<ApiDataResponse> getReadingHistoriesByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<ReadingHistory> readingHistories = readingHistoryService.findByUserId(userId, sortField, sortOrder, page, size);
        PageInfo pageInfo = PageUtils.makePageInfo(readingHistories);
        return ResponseEntity.ok(ApiDataResponse.success(readingHistories.getContent(), pageInfo));
    }

    // Endpoint for deleting a reading history by id
    @DeleteMapping("/reading-history/{id}")
    public ResponseEntity<ApiDataResponse> deleteReadingHistoryById(@PathVariable Integer id) {
        readingHistoryService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse> getUserById(@PathVariable Integer id) {

        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(userService.findById(id)));
    }


    // Endpoint for getting all comments
    @GetMapping("/comments")
    public ResponseEntity<ApiDataResponse> getAllComments() {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(commentService.findAll()));
    }

    // Endpoint for getting a comment by id
    @GetMapping("/comment/{id}")
    public ResponseEntity<ApiDataResponse> getCommentById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(commentService.findById(id)));
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

    @PutMapping("/like-manga/{id}")
    public ResponseEntity<ApiDataResponse> likeManga(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(mangaService.likeManga(id)));
    }

    //update user image
    @PutMapping("/update-image/{id}")
    public ResponseEntity<ApiDataResponse> updateImage(@PathVariable Integer id, @RequestParam String imageUrl) {
        User user = userService.findById(id);
        user.setAvatar(imageUrl);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(userService.update(id, user)));
    }

    // create report the comment (just report comment, not for manga)
    @PostMapping("/report")
    public ResponseEntity<ApiDataResponse> createReport(@RequestBody String content) {
        Report report = new Report();
        report.setContent(content);
        report.setStatus(StatusUtil.PENDING);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(reportService.add(report)));
    }
}

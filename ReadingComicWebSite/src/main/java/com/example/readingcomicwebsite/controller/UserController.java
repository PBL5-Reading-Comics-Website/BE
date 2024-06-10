package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.dto.ReportDto;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.readingcomicwebsite.auth.ApiDataResponse;

import com.example.readingcomicwebsite.model.*;
import com.example.readingcomicwebsite.service.*;
import com.example.readingcomicwebsite.service.impl.WaitingService;
import com.example.readingcomicwebsite.dto.AvatarDto;
import com.example.readingcomicwebsite.model.Comment;
import com.example.readingcomicwebsite.model.Following;
import com.example.readingcomicwebsite.model.Manga;
import com.example.readingcomicwebsite.model.ReadingHistory;
import com.example.readingcomicwebsite.model.Report;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.service.IChapterService;
import com.example.readingcomicwebsite.service.ICommentService;
import com.example.readingcomicwebsite.service.IFollowingService;
import com.example.readingcomicwebsite.service.IMangaService;
import com.example.readingcomicwebsite.service.IReadingHistoryService;
import com.example.readingcomicwebsite.service.IReportService;
import com.example.readingcomicwebsite.service.ITagService;
import com.example.readingcomicwebsite.service.IUserService;
import com.example.readingcomicwebsite.util.PageInfo;
import com.example.readingcomicwebsite.util.PageUtils;
import com.example.readingcomicwebsite.util.StatusUtil;

import lombok.RequiredArgsConstructor;

import java.sql.Date;


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
    private final WaitingService waitingService;

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

    @GetMapping("/is-following-manga")
    public ResponseEntity<ApiDataResponse> isFollowingManga(
            @RequestParam Integer userId,
            @RequestParam Integer mangaId
    ) {
        boolean isFollowing = followingService.isFollowingManga(userId, mangaId);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(isFollowing));
    }

    // following: {userId, mangaId}
    @PostMapping("/following")
    public ResponseEntity<ApiDataResponse> toggleFollow(
            @RequestParam Integer userId,
            @RequestParam Integer mangaId
    ) {
        User user = userService.findById(userId);
        Manga manga = mangaService.findById(mangaId);

        if (followingService.isFollowingManga(userId, mangaId)) {
            // User is following, unfollow
            followingService.deleteByUserAndManga(user, manga);
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta("Unfollowed manga"));
        } else {
            // User is not following, follow
            followingService.add(new Following(user, manga));
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta("Followed manga"));
        }
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

    // add reading history
    @PostMapping("/reading-history")
    public ResponseEntity<ApiDataResponse> addReadingHistory(
            @RequestParam Integer userId,
            @RequestParam Integer mangaId,
            @RequestParam Integer chapterId
    ) {
        Date endAt = new Date(System.currentTimeMillis());
        User user = userService.findById(userId);
        Manga manga = mangaService.findById(mangaId);
        Chapter chapter = chapterService.findById(chapterId);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(readingHistoryService.add(new ReadingHistory(endAt, user, manga, chapter))));
    }

    // Endpoint for deleting a reading history by id
    @DeleteMapping("/reading-history/{id}")
    public ResponseEntity<ApiDataResponse> deleteReadingHistoryById(@PathVariable Integer id) {
        readingHistoryService.deleteById(id);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }

    @GetMapping("/{id}")
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
    public ResponseEntity<ApiDataResponse> likeManga(
            @PathVariable Integer id,
            @RequestParam Integer userId
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(mangaService.likeManga(id, userId)));
    }

    @GetMapping("/is-liked-manga")
    public ResponseEntity<ApiDataResponse> isLikedManga(
            @RequestParam Integer mangaId,
            @RequestParam Integer userId
    ) {
        boolean isLiked = mangaService.isLikedManga(mangaId, userId);
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(isLiked));
    }

    //update user image
    @PutMapping("/update-image/{id}")
    public ResponseEntity<ApiDataResponse> updateImage(@PathVariable Integer id, @RequestBody AvatarDto avatarDto) {
        User user = userService.findById(id);
        user.setAvatar(avatarDto.getImageUrl());
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(userService.update(id, user)));
    }

    @PostMapping("/report")
    public ResponseEntity<ApiDataResponse> createReport(@RequestBody ReportDto reportDto) {
        // Check if a report already exists for this comment
        Report existingReport = reportService.findByComment(commentService.findById(reportDto.getCommentId()));
        if (existingReport != null) {
            return ResponseEntity.ok(ApiDataResponse.error("Report already exists for this comment."));
        }

        Report report = new Report();
        BeanUtils.copyProperties(reportDto, report);
        report.setStatus(StatusUtil.PENDING);
        Manga manga = mangaService.findById(reportDto.getMangaId());
        Comment comment = commentService.findById(reportDto.getCommentId());
        report.setManga(manga);
        report.setComment(comment);

        reportService.add(report);

        // Return success message even if the report already exists
        // (as the report is not created again)
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(report));
    }

    // get all read manga in reading history
    @GetMapping("/read-manga")
    public ResponseEntity<ApiDataResponse> getReadManga(
            @RequestParam Integer userId
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(readingHistoryService.getReadManga(userId)));
    }

    // request to become a poster
    @PostMapping("/waiting")
    public ResponseEntity<ApiDataResponse> requestToBecomePoster(@RequestParam Integer userId) {
        Waiting waiting = new Waiting();
        waiting.setUser(userService.findById(userId));
        if (waitingService.findByUserId(userId) != null) {
            return ResponseEntity.ok(ApiDataResponse.error("You have already requested to become a poster"));
        }
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(waitingService.add(waiting)));
    }
}

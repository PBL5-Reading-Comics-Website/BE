package com.example.readingcomicwebsite.controller;

import com.cloudinary.Api;
import com.example.readingcomicwebsite.auth.ApiDataResponse;
import com.example.readingcomicwebsite.dto.EmailDto;
import com.example.readingcomicwebsite.model.*;
import com.example.readingcomicwebsite.service.*;
import com.example.readingcomicwebsite.service.impl.EmailService;
import com.example.readingcomicwebsite.util.PageInfo;
import com.example.readingcomicwebsite.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.tokens.CommentToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// PublicController handles all public-facing API endpoints
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@CrossOrigin()
public class PublicController {
    private final EmailService emailService;
    private final JavaMailSender javaMailSender;
    private final IUserService userService;
    private final IMangaService mangaService;
    private final IChapterService chapterService;
    private final ICommentService commentService;
    private final IReportService reportService;
    private final ITagService tagService;
    private final IFollowingService followingService;
    private final IReadingHistoryService readingHistoryService;

    // Endpoint for sending an email
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailDto emailDto) throws MailException {
        emailService.sendEmail(emailDto);
        return "Sent successfully...";
    }

    @GetMapping("/manga/newest")
    public ResponseEntity<ApiDataResponse> getTop10NewestManga() {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(mangaService.getTop10NewestManga()));
    }

    @GetMapping("/manga/first-quarter")
    public ResponseEntity<ApiDataResponse> getMangaPublishedInFirstQuarter(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Manga> mangaPage = mangaService.getMangaPublishedInFirstQuarter(page, size, sortField, sortOrder);
        PageInfo pageInfo = PageUtils.makePageInfo(mangaPage);
        return ResponseEntity.ok(ApiDataResponse.success(mangaPage.getContent(), pageInfo));
    }

    @GetMapping("/manga/second-quarter")
    public ResponseEntity<ApiDataResponse> getMangaPublishedInSecondQuarter(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Manga> mangaPage = mangaService.getMangaPublishedInSecondQuarter(page, size, sortField, sortOrder);
        PageInfo pageInfo = PageUtils.makePageInfo(mangaPage);
        return ResponseEntity.ok(ApiDataResponse.success(mangaPage.getContent(), pageInfo));
    }

    @GetMapping("/manga/third-quarter")
    public ResponseEntity<ApiDataResponse> getMangaPublishedInThirdQuarter(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Manga> mangaPage = mangaService.getMangaPublishedInThirdQuarter(page, size, sortField, sortOrder);
        PageInfo pageInfo = PageUtils.makePageInfo(mangaPage);
        return ResponseEntity.ok(ApiDataResponse.success(mangaPage.getContent(), pageInfo));
    }

    @GetMapping("/manga/fourth-quarter")
    public ResponseEntity<ApiDataResponse> getMangaPublishedInFourthQuarter(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Manga> mangaPage = mangaService.getMangaPublishedInFourthQuarter(page, size, sortField, sortOrder);
        PageInfo pageInfo = PageUtils.makePageInfo(mangaPage);
        return ResponseEntity.ok(ApiDataResponse.success(mangaPage.getContent(), pageInfo));
    }

    // Endpoint for getting all users
    @GetMapping("/users")
    public ResponseEntity<ApiDataResponse> getAllUsers() {

        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(userService.findAll()));
    }

    // Endpoint for getting a user by id
    @GetMapping("/user/{id}")
    public ResponseEntity<ApiDataResponse> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(userService.findById(id)));
    }

    // Endpoint for getting all mangas
    @GetMapping("/mangas")
    public ResponseEntity<ApiDataResponse> getAllMangas(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<Manga> mangaPage = mangaService.findAll(sortField, sortOrder, page, size);
        PageInfo pageInfo = PageUtils.makePageInfo(mangaPage);

        return ResponseEntity.ok(ApiDataResponse.success(mangaPage.getContent(), pageInfo));
    }

    // Endpoint for getting a manga by id
    @GetMapping("/manga/{id}")
    public ResponseEntity<ApiDataResponse> getMangaById(@PathVariable Integer id) {
        Manga manga = mangaService.findById(id);
        if (manga == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("manga", manga);
        response.put("tags", tagService.findAllByMangaId(id));
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(response));
    }

    // Endpoint for getting all chapters
    @GetMapping("/chapters")
    public ResponseEntity<ApiDataResponse> getAllChapters(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<Chapter> chapterPage = chapterService.findAll(sortField, sortOrder, page, size);
        PageInfo pageInfo = PageUtils.makePageInfo(chapterPage);
        return ResponseEntity.ok(ApiDataResponse.success(chapterPage.getContent(), pageInfo));
    }

    // Endpoint for getting a chapter by id
    @GetMapping("/chapter/{id}")
    public ResponseEntity<ApiDataResponse> getChapterById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(chapterService.findById(id)));
    }

    @GetMapping("/manga/{id}/chapters")
    public ResponseEntity<ApiDataResponse> getChaptersByMangaId(
            @PathVariable Integer id,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(chapterService.findAllByMangaId(id)));
    }


    // Endpoint for getting all comments
    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return commentService.findAll();
    }

    // Endpoint for getting a comment by id
    @GetMapping("/comment/{id}")
    public Comment getCommentById(@PathVariable Integer id) {
        return commentService.findById(id);
    }

    // Endpoint for deleting a report by id
    @DeleteMapping("/report/{id}")
    public void deleteReportById(@PathVariable Integer id) {
        reportService.deleteById(id);
    }

    // Endpoint for getting all tags
    @GetMapping("/tags")
    public ResponseEntity<ApiDataResponse> getAllTags() {

        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(tagService.findAll()));
    }

    // Endpoint for getting a tag by id
    @GetMapping("/tag/{id}")
    public Tag getTagById(@PathVariable Integer id) {
        return tagService.findById(id);
    }


}
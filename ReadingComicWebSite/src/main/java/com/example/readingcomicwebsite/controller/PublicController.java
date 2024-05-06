package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.dto.EmailDto;
import com.example.readingcomicwebsite.model.*;
import com.example.readingcomicwebsite.service.*;
import com.example.readingcomicwebsite.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.tokens.CommentToken;

import java.util.List;

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
    public List<Manga> getTop10NewestManga() {
        return mangaService.getTop10NewestManga();
    }

    @GetMapping("/manga/first-quarter")
    public List<Manga> getMangaPublishedInFirstQuarter() {
        return mangaService.getMangaPublishedInFirstQuarter();
    }

    @GetMapping("/manga/second-quarter")
    public List<Manga> getMangaPublishedInSecondQuarter() {
        return mangaService.getMangaPublishedInSecondQuarter();
    }

    @GetMapping("/manga/third-quarter")
    public List<Manga> getMangaPublishedInThirdQuarter() {
        return mangaService.getMangaPublishedInThirdQuarter();
    }

    @GetMapping("/manga/fourth-quarter")
    public List<Manga> getMangaPublishedInFourthQuarter() {
        return mangaService.getMangaPublishedInFourthQuarter();
    }

    // Endpoint for getting all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // Endpoint for getting a user by id
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }


    // Endpoint for getting all mangas
    @GetMapping("/mangas")
    public List<Manga> getAllMangas() {
        return mangaService.findAll();
    }

    // Endpoint for getting a manga by id
    @GetMapping("/manga/{id}")
    public Manga getMangaById(@PathVariable Integer id) {
        return mangaService.findById(id);
    }

    // Endpoint for getting all chapters
    @GetMapping("/chapters")
    public List<Chapter> getAllChapters() {
        return chapterService.findAll();
    }

    // Endpoint for getting a chapter by id
    @GetMapping("/chapter/{id}")
    public Chapter getChapterById(@PathVariable Integer id) {
        return chapterService.findById(id);
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
    public List<Tag> getAllTags() {
        return tagService.findAll();
    }

    // Endpoint for getting a tag by id
    @GetMapping("/tag/{id}")
    public Tag getTagById(@PathVariable Integer id) {
        return tagService.findById(id);
    }


}
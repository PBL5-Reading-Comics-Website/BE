package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.dto.EmailDto;
import com.example.readingcomicwebsite.service.*;
import com.example.readingcomicwebsite.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

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

    // Endpoint for getting all users
    @GetMapping("/users")
    public Object getAllUsers() {
        return userService.findAll();
    }

    // Endpoint for getting a user by id
    @GetMapping("/user/{id}")
    public Object getUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    // Endpoint for deleting a user by id
    @DeleteMapping("/user/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteById(id);
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

    // Endpoint for deleting a manga by id
    @DeleteMapping("/manga/{id}")
    public void deleteMangaById(@PathVariable Integer id) {
        mangaService.deleteById(id);
    }

    // Endpoint for getting all chapters
    @GetMapping("/chapters")
    public Object getAllChapters() {
        return chapterService.findAll();
    }

    // Endpoint for getting a chapter by id
    @GetMapping("/chapter/{id}")
    public Object getChapterById(@PathVariable Integer id) {
        return chapterService.findById(id);
    }

    // Endpoint for deleting a chapter by id
    @DeleteMapping("/chapter/{id}")
    public void deleteChapterById(@PathVariable Integer id) {
        chapterService.deleteById(id);
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

    // Endpoint for getting all reports
    @GetMapping("/reports")
    public Object getAllReports() {
        return reportService.findAll();
    }

    // Endpoint for getting a report by id
    @GetMapping("/report/{id}")
    public Object getReportById(@PathVariable Integer id) {
        return reportService.findById(id);
    }

    // Endpoint for deleting a report by id
    @DeleteMapping("/report/{id}")
    public void deleteReportById(@PathVariable Integer id) {
        reportService.deleteById(id);
    }

    // Endpoint for getting all tags
    @GetMapping("/tags")
    public Object getAllTags() {
        return tagService.findAll();
    }

    // Endpoint for getting a tag by id
    @GetMapping("/tag/{id}")
    public Object getTagById(@PathVariable Integer id) {
        return tagService.findById(id);
    }

    // Endpoint for deleting a tag by id
    @DeleteMapping("/tag/{id}")
    public void deleteTagById(@PathVariable Integer id) {
        tagService.deleteById(id);
    }

}
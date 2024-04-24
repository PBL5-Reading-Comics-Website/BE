package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.dto.EmailDto;
import com.example.readingcomicwebsite.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@CrossOrigin()
public class PublicController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/1")
    public String get1() {
        return "1";
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailDto emailDto) throws MailException {
        emailService.sendEmail(emailDto);
        return "Sent successfully...";
    }
}

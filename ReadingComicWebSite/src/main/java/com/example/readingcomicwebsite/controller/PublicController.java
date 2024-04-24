package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.dto.EmailDto;
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
    private JavaMailSender javaMailSender;

    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailDto emailDto) throws MailException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        javaMailSender.send(mimeMessage);
        return "Email sent successfully...";
    }
}

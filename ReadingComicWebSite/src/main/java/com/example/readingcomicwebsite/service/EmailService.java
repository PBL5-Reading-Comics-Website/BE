package com.example.readingcomicwebsite.service;

import com.example.readingcomicwebsite.dto.EmailDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailDto emailDto) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDto.getTo());
            message.setSubject(emailDto.getSubject());
            message.setText(emailDto.getSubject());
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception);
            exception.printStackTrace();
        }

    }
}
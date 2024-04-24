package com.example.readingcomicwebsite.util;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;



@Component
public class EmailUtil {
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public void sendSetPasswordEmail(String email) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//        try {
//            mimeMessageHelper.setTo(email);
//            mimeMessageHelper.setSubject("Set password");
//            mimeMessageHelper.setText("""
//                    <div>
//                      <a href="http://localhost:8080/forgot-password?email=%s" target="_blank">click link to set new password</a>
//                    </div>
//                    """.formatted(email), true);
//        } catch (javax.mail.MessagingException e) {
//            throw new RuntimeException(e);
//        }
//        javaMailSender.send(mimeMessage);
//    }
}

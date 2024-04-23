package com.example.readingcomicwebsite.util;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    public void sendSetPasswordEmail(String email) throws MessagingException {
        System.out.println("Email sent to " + email);
    }
}

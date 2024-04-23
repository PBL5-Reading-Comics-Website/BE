package com.example.readingcomicwebsite.auth;

import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CrossOrigin()
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
}

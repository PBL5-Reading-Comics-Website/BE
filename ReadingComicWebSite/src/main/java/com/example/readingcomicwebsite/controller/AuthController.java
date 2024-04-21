package com.example.readingcomicwebsite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/demo")
public class AuthController {
    @GetMapping
    public ResponseEntity<String> sayAhihi() {
        return ResponseEntity.ok("Say Ahihi from security!");
    }
}

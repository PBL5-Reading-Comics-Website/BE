package com.example.readingcomicwebsite.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfiguration {
    private final String CLOUD_NAME = "";

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary("cloudinary://api_key:api_secret@cloud_name");
    }
}

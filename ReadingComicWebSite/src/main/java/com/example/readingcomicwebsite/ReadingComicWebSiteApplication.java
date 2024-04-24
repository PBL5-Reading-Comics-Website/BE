package com.example.readingcomicwebsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ReadingComicWebSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadingComicWebSiteApplication.class, args);
    }

}

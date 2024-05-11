package com.example.readingcomicwebsite.dto;

import lombok.*;

import javax.persistence.Column;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MangaDto {
    private String name;
    private String publishingCompany;
    private String author;
    private String artist;
    private String description;
    private String coverImage;
}

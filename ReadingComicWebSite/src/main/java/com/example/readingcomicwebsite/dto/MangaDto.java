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
    private String coverImage;
    private String author;
    private String artist;
}

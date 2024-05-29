package com.example.readingcomicwebsite.dto;

import com.example.readingcomicwebsite.model.Tag;
import lombok.*;

import javax.persistence.Column;
import java.util.List;

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
    private List<Tag> tags;
}

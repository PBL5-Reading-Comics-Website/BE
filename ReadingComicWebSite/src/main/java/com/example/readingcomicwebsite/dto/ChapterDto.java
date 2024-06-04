package com.example.readingcomicwebsite.dto;

import com.example.readingcomicwebsite.model.Tag;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDto {
    private String name;
    private Integer number;
    private List<String> images;
}

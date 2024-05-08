package com.example.readingcomicwebsite.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDto {
    private String name;
    private Integer number;
}

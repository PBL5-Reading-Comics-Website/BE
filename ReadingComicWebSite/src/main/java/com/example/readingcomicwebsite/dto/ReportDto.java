package com.example.readingcomicwebsite.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private String content;
    private Integer mangaId;
    private Integer commentId;
}

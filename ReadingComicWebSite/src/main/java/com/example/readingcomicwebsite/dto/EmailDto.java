package com.example.readingcomicwebsite.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private String to;
    private String subject;
    private String text ;
}

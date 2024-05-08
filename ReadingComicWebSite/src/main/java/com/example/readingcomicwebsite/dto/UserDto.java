package com.example.readingcomicwebsite.dto;

import lombok.*;

import java.sql.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private Date dateOfBirth;
    private Boolean gender;
    private String email;
}

package com.example.readingcomicwebsite.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CrossOrigin()
public class UpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
}

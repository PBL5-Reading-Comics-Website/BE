package com.example.readingcomicwebsite.auth;

import com.example.readingcomicwebsite.config.JwtService;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.repository.UserRepository;
import com.example.readingcomicwebsite.util.EmailUtil;
import com.example.readingcomicwebsite.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Date;

@Service
@RequiredArgsConstructor
@CrossOrigin()
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService = new JwtService();
    private AuthenticationManager authenticationManager;
    private final EmailUtil emailUtil;

    public AuthenticationResponse register(RegisterRequest request) {
        String DEFAULT_AVATAR = "classpath:static/default-avatar.jpg";
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .name(request.getUsername())
                .avatar(DEFAULT_AVATAR)
                .dateOfBirth(Date.valueOf(java.time.LocalDate.now()))
                .gender(true)
                .registrationDate(Date.valueOf(java.time.LocalDate.now()))
                .role(Role.USER)

                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticaitonRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public String forgotPassword(String email) {
        repository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found with email: " + email)
                );
//        try {
//            emailUtil.sendSetPasswordEmail(email);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Unable to send set password email! Please try again.");
//        }
        return "Please check your email to set new password!";
    }

    public String updatePassword(User user) {
        User userDb = repository.getById(user.getId());
        if (userDb == null)
            return null;
        BeanUtils.copyProperties(user, userDb, "id");
        repository.updateUserInfo(userDb.getUsername(), userDb.getEmail(), userDb.getPassword(), userDb.getId())
                .orElseThrow(
                        () -> new RuntimeException("User not found with id: " + user.getId())
                );
        return "Information updated successfully!";
    }
}

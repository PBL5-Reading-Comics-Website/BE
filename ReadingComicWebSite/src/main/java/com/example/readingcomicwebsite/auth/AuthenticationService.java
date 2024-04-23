package com.example.readingcomicwebsite.auth;

import com.example.readingcomicwebsite.config.JwtService;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.repository.UserRepository;
import com.example.readingcomicwebsite.util.Role;
import lombok.RequiredArgsConstructor;
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
    private JwtService jwtService = new JwtService();
    private AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        var account = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .registrationDate(Date.valueOf(java.time.LocalDate.now()))
                .role(Role.USER)

                .build();
        repository.save(account);
        var jwtToken = jwtService.generateToken(account);
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
        var account = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        var account = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}

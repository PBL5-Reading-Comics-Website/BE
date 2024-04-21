package com.example.readingcomicwebsite.auth;

import com.example.readingcomicwebsite.config.JwtService;
import com.example.readingcomicwebsite.model.Account;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.repository.AccountRepository;
import com.example.readingcomicwebsite.repository.UserRepository;
import com.example.readingcomicwebsite.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        var account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
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
}

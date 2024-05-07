package com.example.readingcomicwebsite.auth;

import com.example.readingcomicwebsite.config.JwtService;
import com.example.readingcomicwebsite.model.User;
import com.example.readingcomicwebsite.model.UserDetailsCustome;
import com.example.readingcomicwebsite.repository.UserRepository;
import com.example.readingcomicwebsite.util.EmailUtil;
import com.example.readingcomicwebsite.util.Role;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
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
                .role(Role.ROLE_USER)
                .build();
        repository.save(user);

        // Create UserDetailsCustome object from User
        UserDetailsCustome userDetails = new UserDetailsCustome(user.getId(), user.getRole().name());

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//        var user = repository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));
//        var jwtToken = jwtService.generateToken(new UserDetailsCustome(user.getId(), user.getRole().name()));
//        return AuthenticationResponse.builder().token(jwtToken).build();
//    }

    public AuthenticationResponse login(LoginRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));
        var jwtToken = jwtService.generateToken(new UserDetailsCustome(user.getId(), user.getRole().name()));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public String forgotPassword(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        // Implement your logic for sending set password email here
        return "Please check your email to set a new password!";
    }

    public String updatePassword(User user) {
        User userDb = repository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + user.getId()));
        userDb.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(userDb);
        return "Password updated successfully!";
    }
}

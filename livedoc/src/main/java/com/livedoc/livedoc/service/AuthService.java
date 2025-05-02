package com.livedoc.livedoc.service;

import java.time.Instant;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.livedoc.livedoc.exception.ErrorCode;
import com.livedoc.livedoc.exception.LiveDocException;
import com.livedoc.livedoc.model.AuthRequest;
import com.livedoc.livedoc.model.AuthResponse;
import com.livedoc.livedoc.model.RegisterRequest;
import com.livedoc.livedoc.model.Role;
import com.livedoc.livedoc.model.Token;
import com.livedoc.livedoc.model.User;
import com.livedoc.livedoc.repository.RoleRepository;
import com.livedoc.livedoc.repository.TokenRepository;
import com.livedoc.livedoc.repository.UserRepository;
import com.livedoc.livedoc.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new LiveDocException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Invalid role specified"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .createdAt(Instant.now().toEpochMilli())
                .build();

        userRepository.save(user);
        return "User registered successfully!";
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new LiveDocException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = UUID.randomUUID().toString(); // or you can generate JWT refresh tokens

        // Save refresh token in DB
        Token token = Token.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60).toEpochMilli()) // 7 days validity
                .revoked(false)
                .createdAt(Instant.now().toEpochMilli())
                .build();
        tokenRepository.save(token);

        return new AuthResponse(accessToken, refreshToken, user.getUsername(), user.getRole().getName());
    }

    public AuthResponse refreshToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshTokenAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid or revoked refresh token"));
    
        if (token.getExpiresAt() < Instant.now().toEpochMilli()) {
            throw new RuntimeException("Refresh token expired, please login again");
        }
    
        User user = token.getUser();
    
        // Generate new Access Token
        String newAccessToken = jwtUtil.generateToken(user);
    
        // Optionally, rotate refresh token
        // In production, you SHOULD rotate refresh tokens
        String newRefreshToken = UUID.randomUUID().toString();
    
        // Mark old token as revoked
        token.setRevoked(true);
        tokenRepository.save(token);
    
        // Save new refresh token
        Token newToken = Token.builder()
                .user(user)
                .refreshToken(newRefreshToken)
                .expiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60).toEpochMilli())
                .revoked(false)
                .createdAt(Instant.now().toEpochMilli())
                .build();
        tokenRepository.save(newToken);
    
        return new AuthResponse(newAccessToken, newRefreshToken, user.getUsername(), user.getRole().getName());
    }

    public void logout(String refreshToken) {
        Token token = tokenRepository.findByRefreshTokenAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    
        token.setRevoked(true);
        tokenRepository.save(token);
    }
    
}

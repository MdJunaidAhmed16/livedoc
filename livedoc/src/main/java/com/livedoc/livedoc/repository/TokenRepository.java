package com.livedoc.livedoc.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.livedoc.livedoc.model.Token;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    
    List<Token> findAllByUserIdAndRevokedFalse(UUID userId);

    Optional<Token> findByRefreshTokenAndRevokedFalse(String refreshToken);
}

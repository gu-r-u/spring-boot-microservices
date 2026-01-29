package com.microservice.authorization.service;

import com.microservice.authorization.entity.RefreshToken;
import com.microservice.authorization.repository.RefreshTokenRepository;
import com.microservice.authorization.security.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${jwt.refresh-exp-days:7}")
    private long refreshTokenDays;

    /**
     * Create and persist a refresh token
     */
    public String create(RefreshToken tokenOj) {

        String rawToken = UUID.randomUUID().toString();
        String hash = TokenHashUtil.hash(rawToken);

        RefreshToken token = RefreshToken.builder()
                .userId(tokenOj.getUserId())
                .tokenHash(hash)
                .expiryAt( tokenOj.getExpiryAt() == null ?
                        Instant.now()
                                .plus(refreshTokenDays, ChronoUnit.DAYS)
                        : tokenOj.getExpiryAt()
                )
                .revoked(false)
                .createdAt(Instant.now())
                .build();

        repository.save(token);

        // IMPORTANT: return RAW token to client
        return rawToken;
    }

    /**
     * Validate refresh token (used later for refresh)
     */
    public RefreshToken validate(String rawToken) {

        String hash = TokenHashUtil.hash(rawToken);

        RefreshToken token = repository.findByTokenHash(hash)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (token.getExpiryAt().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    /**
     * Revoke all tokens for user (logout)
     */
    @Transactional
    public void revokeAllForUser(String userId) {
        repository.revokeAllByUserId(userId);
    }

    public String rotate(String rawRefreshToken) {

        // 1. Validate old refresh token (DB + hash)
        RefreshToken existingToken = validate(rawRefreshToken);

        // 2. Revoke old token (one-time use)
        existingToken.setRevoked(true);
        repository.save(existingToken);

        // 3. Create and persist new refresh token
        return create(existingToken);
    }

}

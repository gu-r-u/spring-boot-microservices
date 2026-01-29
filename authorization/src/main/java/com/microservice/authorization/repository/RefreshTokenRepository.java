package com.microservice.authorization.repository;

import com.microservice.authorization.entity.RefreshToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
        UPDATE RefreshToken r
        SET r.revoked = true
        WHERE r.userId = :userId
    """)
    void revokeAllByUserId(@Param("userId") String userId);

    @Modifying
    @Query("""
        DELETE FROM RefreshToken r
        WHERE r.expiryAt < :now
    """)
    void deleteExpired(@Param("now") Instant now);
}

package com.microservice.authorization.service;

import com.microservice.authorization.dto.AuthResponse;
import com.microservice.authorization.dto.LoginRequest;
import com.microservice.authorization.dto.RefreshTokenRequest;
import com.microservice.authorization.entity.RefreshToken;
import com.microservice.authorization.entity.User;
import com.microservice.authorization.repository.UserRepository;
import com.microservice.authorization.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String userId = user.getId().toString();

        String accessToken = jwtUtil.generateToken(
                userId,
                user.getRoles()
        );
        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setUserId(userId);

        String refreshToken = refreshTokenService.create(refreshTokenObj);

        return new AuthResponse(accessToken, refreshToken);
    }
    public AuthResponse refresh(RefreshTokenRequest request) {

        // 1. Validate old refresh token
        RefreshToken oldToken =
                refreshTokenService.validate(request.getRefreshToken());

        String userId = oldToken.getUserId();

        // 2. Rotate refresh token (revoke old + create new)
        String newRefreshToken =
                refreshTokenService.rotate(request.getRefreshToken());

        // 3. Load user (for roles)
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Generate new access token
        String newAccessToken = jwtUtil.generateToken(
                userId,
                user.getRoles()
        );

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String userId) {
        refreshTokenService.revokeAllForUser(userId);
    }

}

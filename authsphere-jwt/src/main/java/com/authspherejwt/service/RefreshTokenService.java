package com.authspherejwt.service;

import com.authspherejwt.entity.RefreshToken;
import com.authspherejwt.entity.User;
import com.authspherejwt.exception.InvalidRefreshTokenException;
import com.authspherejwt.repository.RefreshTokenRepository;
import com.authspherejwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service responsible for handling refresh token lifecycle.
 *
 * This class contains the core business logic for:
 * - Creating refresh tokens during login
 * - Validating refresh tokens during token refresh
 * - Deleting refresh tokens during logout
 *
 * Unlike JWT access tokens (which are stateless),
 * refresh tokens are stored in the database to allow
 * controlled session management.
 *
 * This enables:
 * - Token revocation (logout)
 * - Expiry validation
 * - Secure session handling
 *
 * Author: Trupti Patil
 */
@Service
public class RefreshTokenService {

    /**
     * Duration for refresh token validity (in milliseconds).
     * Configured in application.properties
     */
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDuration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new refresh token for a given username.
     *
     * Flow:
     * 1. Fetch user from database
     * 2. Generate a random token (UUID)
     * 3. Set expiry time (current time + configured duration)
     * 4. Save token in database
     *
     * This method is typically called during login.
     *
     * Each refresh token represents one active session.
     *
     * @param username the username of the authenticated user
     * @return saved RefreshToken entity
     */
    public RefreshToken createRefreshToken(String username) {

        /*
         * STEP 1: Fetch user from database
         * ---------------------------------
         * We first validate if the user exists in the system.
         * If user is not found, we throw an exception immediately.
         */
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        /*
         * STEP 2: Check if refresh token already exists (UPSERT logic)
         * -------------------------------------------------------------
         * We ensure only ONE active refresh token per user.
         * If token exists → UPDATE it
         * If not → CREATE new one
         */
        Optional<RefreshToken> existingToken =
                refreshTokenRepository.findByUser(user);

        RefreshToken token;

        if (existingToken.isPresent()) {

            /*
             * UPDATE FLOW
             * -----------
             * Reuse existing token record and update values.
             * This avoids duplicate DB entries and maintains session continuity.
             */
            token = existingToken.get();

            token.setToken(UUID.randomUUID().toString());

            token.setExpiryDate(
                    Instant.now().plusMillis(refreshTokenDuration)
            );

        } else {

            /*
             * CREATE FLOW
             * -----------
             * First-time login scenario where no refresh token exists.
             */
            token = new RefreshToken();

            token.setUser(user);

            token.setToken(UUID.randomUUID().toString());

            token.setExpiryDate(
                    Instant.now().plusMillis(refreshTokenDuration)
            );
        }

        /*
         * STEP 3: Save token (Insert or Update)
         * --------------------------------------
         * JPA handles both insert/update automatically based on entity state.
         */
        return refreshTokenRepository.save(token);
    }    

    /**
     * Validates a refresh token received from the client.
     *
     * Flow:
     * 1. Look up token in database
     * 2. If not found -> invalid token
     * 3. Check expiry time
     * 4. If expired:
     *    - delete token from database
     *    - throw exception
     * 5. If valid -> return token
     *
     * This method is used in /auth/refresh API.
     *
     * @param token the refresh token string from client
     * @return valid RefreshToken entity
     */
    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRefreshTokenException("Refresh token expired");
        }

        return refreshToken;
    }

    /**
     * Deletes refresh token associated with a user.
     *
     * Flow:
     * 1. Find token(s) linked to user
     * 2. Remove from database
     *
     * This is used during logout.
     *
     * Effect:
     * - Prevents future access token generation
     * - Terminates user session
     *
     * Note:
     * Existing JWT access tokens may still work until they expire,
     * but no new tokens can be issued.
     *
     * @param user the user whose session should be terminated
     */
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
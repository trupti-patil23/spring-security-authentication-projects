package com.authspherejwt.repository;

import com.authspherejwt.entity.RefreshToken;
import com.authspherejwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for handling RefreshToken persistence.
 *
 * This repository is used to manage refresh tokens stored in the database.
 * Unlike JWT access tokens (which are stateless), refresh tokens are stored
 * so that the system can control user sessions.
 *
 * It supports:
 * - Validating refresh tokens during token refresh flow
 * - Deleting tokens during logout
 * - Managing session lifecycle
 *
 * Extends JpaRepository to provide standard CRUD operations.
 *
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds a refresh token by its token value.
     *
     * @param token the refresh token received from client
     * @return Optional containing the RefreshToken if found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Deletes the refresh token associated with a user.
     *
     * @param user the user whose refresh token should be deleted
     */
    void deleteByUser(User user);

    /**
     * Finds a refresh token by user.
     *
     * This method is used for UPSERT logic in authentication flow.
     *
     * Flow:
     * 1. During login, system checks if refresh token already exists for user
     * 2. If EXISTS → update the existing token
     * 3. If NOT EXISTS → create a new refresh token
     *
     * @param user the user whose refresh token is being fetched
     * @return Optional containing RefreshToken if present
     */
    Optional<RefreshToken> findByUser(User user);
}
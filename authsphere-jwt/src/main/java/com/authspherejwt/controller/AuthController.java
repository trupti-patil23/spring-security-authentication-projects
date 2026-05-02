package com.authspherejwt.controller;

import com.authspherejwt.dto.LoginRequestDTO;
import com.authspherejwt.dto.AuthResponseDTO;
import com.authspherejwt.dto.RegisterRequestDTO;
import com.authspherejwt.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController handles all authentication APIs.
 *
 * Responsibilities:
 * - Receive HTTP requests
 * - Delegate business logic to AuthService
 * - Return responses
 *
 * IMPORTANT:
 * No business logic should be written here.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * REGISTER API
     *
     * Flow:
     * 1. Validate request body (@Valid)
     * 2. Call AuthService.register()
     * 3. Return success response
     */
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }

    /**
     * LOGIN API
     *
     * Flow:
     * 1. Receive username & password
     * 2. Delegate authentication to AuthService
     * 3. Return JWT token if valid
     */
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
    
    /**
     * Endpoint to refresh access token using refresh token.
     *
     * API: POST /auth/refresh
     *
     * Flow:
     * - Client sends refresh token in request body
     * - Controller calls service layer to validate and generate new token
     * - Returns new access token to client
     *
     * @param refreshTokenRequest refresh token string
     * @return AuthResponseDTO with new access token
     */
    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@RequestBody String refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
    
    /**
     * LOGOUT FLOW
     *
     * API: POST /auth/logout
     *
     * Flow:
     * 1. Client sends logout request with username
     * 2. Controller calls service layer
     * 3. Service deletes refresh token from database
     * 4. User session is terminated
     *
     * Note:
     * - Access tokens (JWT) cannot be invalidated immediately
     * - Logout works by removing refresh token
     * - After logout, user cannot generate new access tokens
     *
     * @param username username of the user
     * @return success message
     */
    @PostMapping("/logout")
    public String logout(@RequestParam String username) {
        authService.logout(username);
        return "Logged out successfully";
    }
}
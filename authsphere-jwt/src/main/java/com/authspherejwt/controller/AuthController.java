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
}
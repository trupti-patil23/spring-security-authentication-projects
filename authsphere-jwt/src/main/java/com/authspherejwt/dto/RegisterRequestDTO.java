package com.authspherejwt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for user registration request.
 *
 * This class is used to receive and validate user input
 * during registration API call.
 */
public class RegisterRequestDTO {

    /**
     * Username must not be empty or null.
     */
    @NotBlank(message = "Username cannot be empty")
    private String username;

    /**
     * Strong password rules:
     * - At least 1 uppercase letter
     * - At least 1 lowercase letter
     * - At least 1 number
     * - At least 1 special character
     * - Minimum 6 characters
     */
    @NotBlank(message = "Password cannot be empty")
    @Pattern(
    	    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    	    message = "Password must be at least 8 characters long and contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character"
    )
    private String password;

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


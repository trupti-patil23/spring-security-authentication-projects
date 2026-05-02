package com.authspherejwt.exception;

/**
 * Custom exception for handling invalid or expired refresh tokens.
 *
 * Purpose:
 * This exception is thrown when the refresh token provided by the client
 * is not valid during the refresh flow.
 *
 * Scenarios where this exception is used:
 * - Refresh token does not exist in database
 * - Refresh token is expired
 * - Refresh token was deleted (e.g., after logout)
 *
 */
public class InvalidRefreshTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	/**
     * Constructor to initialize exception with custom message.
     *
     * @param message error message describing why the token is invalid
     */
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}

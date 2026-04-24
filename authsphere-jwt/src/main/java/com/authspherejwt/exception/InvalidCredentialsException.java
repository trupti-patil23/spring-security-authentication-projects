package com.authspherejwt.exception;

/**
 * Custom exception for invalid login credentials.
 * Thrown when username or password is incorrect.
 */
public class InvalidCredentialsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidCredentialsException(String message) {
        super(message);
    }
}
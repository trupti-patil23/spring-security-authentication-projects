package com.authspherejwt.exception;

/**
 * Exception thrown when JWT token is invalid or expired.
 */
public class InvalidTokenException extends RuntimeException {

  	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String message) {
        super(message);
    }
}

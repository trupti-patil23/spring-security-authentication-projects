package com.authspherejwt.exception;

/**
 * Exception thrown when trying to register a user that already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(String message) {
        super(message);
    }
}

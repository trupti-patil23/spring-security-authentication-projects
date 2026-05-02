package com.authspherejwt.dto;

/**
 * Data Transfer Object for authentication response.
 *
 * This DTO is returned to the client after successful authentication
 * (login) and also during token refresh operations.
 *
 * It contains:
 * - Access Token (JWT): Short-lived token used for API authentication
 * - Refresh Token: Long-lived token used to obtain new access tokens
 *
 * Flow:
 * 1. During login:
 *    - Both accessToken and refreshToken are returned
 *
 * 2. During refresh:
 *    - A new accessToken is returned
 *    - Refresh token may remain same or be rotated (based on implementation)
 *
 * This structure enables a hybrid authentication model:
 * - Stateless JWT for API calls
 * - Stateful refresh token for session management
 */
public class AuthResponseDTO {
	 /**
     * JWT Access Token.
     *
     * Short-lived token used in Authorization header for API requests.
     * Example:
     * Authorization: Bearer <accessToken>
     */
	private String accessToken;
	
	/**
     * Refresh Token.
     *
     * Long-lived token used to generate new access tokens
     * when the current access token expires.
     */
    private String refreshToken;

    /**
     * Constructor to initialize both tokens.
     *
     * @param accessToken  JWT access token
     * @param refreshToken refresh token string
     */
    public AuthResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

	/* =========================
     *    Getters and Setters
     * =========================*/

    public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getRefreshToken() {
	    return refreshToken;
	}

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}

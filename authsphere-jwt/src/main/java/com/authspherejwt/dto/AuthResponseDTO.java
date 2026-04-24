package com.authspherejwt.dto;

/**
 * DTO for sending JWT token response
 */
public class AuthResponseDTO {
	private String accessToken;

	public AuthResponseDTO(String accessToken) {
		super();
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}

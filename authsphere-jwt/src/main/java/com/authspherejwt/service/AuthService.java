package com.authspherejwt.service;

import com.authspherejwt.dto.LoginRequestDTO;
import com.authspherejwt.dto.AuthResponseDTO;
import com.authspherejwt.dto.RegisterRequestDTO;
import com.authspherejwt.entity.RefreshToken;
import com.authspherejwt.entity.User;
import com.authspherejwt.exception.InvalidCredentialsException;
import com.authspherejwt.exception.UserAlreadyExistsException;
import com.authspherejwt.repository.UserRepository;
import com.authspherejwt.security.JwtService;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService handles all authentication and authorization related business logic.
 *
 * 1. User Registration
 *    - Creating new user accounts
 *    - Encrypting passwords using PasswordEncoder before storing in database
 *
 * 2. User Authentication (Login)
 *    - Validating user credentials using AuthenticationManager
 *    - Generating JWT access token after successful authentication
 *    - Generating refresh token for session management
 *
 * 3. Token Management
 *    - Generating JWT access tokens (stateless authentication)
 *    - Coordinating refresh token creation via RefreshTokenService
 *
 * 4. Session Management
 *    - Supporting token refresh flow (issue new access token using refresh token)
 *    - Handling logout by revoking refresh tokens
 */
@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;

	/**
	 * Constructor-based Dependency Injection for AuthService.
	 *
	 * This service handles authentication and token generation logic.
	 *
	 * Dependencies:
	 * - UserRepository:
	 *   Used for database operations related to User entity (fetching users during login/registration)
	 *
	 * - PasswordEncoder:
	 *   Used to securely hash passwords before storing in the database
	 *
	 * - AuthenticationManager:
	 *   Core Spring Security component used to authenticate user credentials during login
	 *
	 * - JwtService:
	 *   Responsible for generating and validating JWT access tokens (stateless authentication)
	 *
	 * - RefreshTokenService:
	 *   Handles creation, validation, and deletion of refresh tokens (stateful session management)
	 */
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService,  RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
	}

	/**
	 * REGISTER FLOW
	 *
	 * Steps: 1. Check if user already exists 
	 *        2. Encode password 
	 *        3. Save user in database 
	 *        4. Return success message
	 */
	public String register(RegisterRequestDTO request) {

		// 1. Check duplicate user
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new UserAlreadyExistsException("User already exists: " + request.getUsername());
		}

		// 2. Create new user entity
		User user = new User();
		user.setUsername(request.getUsername());

		// 3. Encode password (NEVER store plain text)
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		// 4. Assign default role
		user.setRole("ROLE_USER");

		// 5. Save to DB
		userRepository.save(user);

		return "User registered successfully";
	}

	/**
	 * LOGIN FLOW (Access Token + Refresh Token)
	 *
	 * Steps:
	 * 1. Authenticate user using AuthenticationManager
	 * 2. If credentials are valid:
	 *    - Generate Access Token (JWT - short-lived)
	 *    - Generate Refresh Token (long-lived, stored in DB)
	 * 3. Return both tokens to client
	 *
	 * Why both tokens?
	 * - Access Token is used for API authentication
	 * - Refresh Token is used to generate new access tokens without re-login
	 *
	 * This approach provides:
	 * - Stateless authentication (JWT)
	 * - Stateful session control (Refresh Token)
	 */
	public AuthResponseDTO login(LoginRequestDTO request) {

		try {
			// 1. Authenticate credentials (Spring Security handles validation)
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException ex) {
			// Convert Spring exception to custom exception
			throw new InvalidCredentialsException("Invalid username or password");
		}

		// 2. Generate JWT token after successful authentication
		String accessToken = jwtService.generateToken(request.getUsername());

		  // 3. Generate Refresh Token (long-lived, stored in database)
	    RefreshToken refreshToken = refreshTokenService
	            .createRefreshToken(request.getUsername());

	    // 4. Return both tokens to client
	    return new AuthResponseDTO(accessToken, refreshToken.getToken());
	}
	
	/**
	 * REFRESH TOKEN FLOW
	 *
	 * This method handles generation of a new access token using a valid refresh token.
	 *
	 * Flow:
	 * 1. Client sends refresh token
	 * 2. Validate refresh token using RefreshTokenService
	 *    - Check if token exists in database
	 *    - Check if token is expired
	 * 3. If valid:
	 *    - Extract associated user
	 *    - Generate new JWT access token
	 * 4. Return new access token (and refresh token if needed)
	 *
	 * This avoids forcing the user to login again when access token expires.
	 *
	 * @param refreshTokenRequest the refresh token received from client
	 * @return AuthResponseDTO containing new access token
	 */
	public AuthResponseDTO refreshToken(String refreshTokenRequest) {

	    // 1. Validate refresh token (existence + expiry check)
	    RefreshToken refreshToken =
	            refreshTokenService.verifyToken(refreshTokenRequest);

	    // 2. Generate new access token using associated user
	    String newAccessToken = jwtService
	            .generateToken(refreshToken.getUser().getUsername());

	    // 3. Return new tokens (refresh token reused in this implementation)
	    return new AuthResponseDTO(newAccessToken, refreshToken.getToken());
	}
	
	/**
	 * Handles logout logic by removing user's refresh token.
	 *
	 * Flow:
	 * 1. Fetch user from database using username
	 * 2. Delete refresh token associated with the user
	 *
	 * Effect:
	 * - Prevents generation of new access tokens
	 * - Ends user session
	 *
	 * Important:
	 * - Existing access tokens may still work until they expire
	 * - This is expected behavior in JWT-based systems
	 *
	 * @param username the username of the user logging out
	 */
    @Transactional
	public void logout(String username) {

	    // 1. Fetch user from database
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // 2. Delete refresh token (session revoked)
	    refreshTokenService.deleteByUser(user);
	}
	
	
}

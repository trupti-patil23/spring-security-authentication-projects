package com.authspherejwt.service;

import com.authspherejwt.dto.LoginRequestDTO;
import com.authspherejwt.dto.AuthResponseDTO;
import com.authspherejwt.dto.RegisterRequestDTO;
import com.authspherejwt.entity.User;
import com.authspherejwt.exception.InvalidCredentialsException;
import com.authspherejwt.exception.UserAlreadyExistsException;
import com.authspherejwt.repository.UserRepository;
import com.authspherejwt.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService handles authentication business logic.
 *
 * Responsibilities: - User registration - User login - Password encoding - JWT
 * generation
 */
@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	/**
	 * Constructor-based Dependency Injection for AuthService.
	 *
	 * Dependencies: - UserRepository: Used for database operations related to User
	 * entity - PasswordEncoder: Used to securely encode passwords before saving to
	 * DB - AuthenticationManager: Used by Spring Security to authenticate login
	 * credentials - JwtService: Used to generate and validate JWT tokens after
	 * authentication
	 */
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	/**
	 * REGISTER FLOW
	 *
	 * Steps: 1. Check if user already exists 2. Encode password 3. Save user in
	 * database 4. Return success message
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
	 * LOGIN FLOW
	 *
	 * Steps: 1. Authenticate user using AuthenticationManager 2. If valid →
	 * generate JWT token 3. Return token
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
		String token = jwtService.generateToken(request.getUsername());

		// 3. Return response
		return new AuthResponseDTO(token);
	}
}

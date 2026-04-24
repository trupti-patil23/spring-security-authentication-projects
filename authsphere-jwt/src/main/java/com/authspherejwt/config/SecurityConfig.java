package com.authspherejwt.config;

import com.authspherejwt.security.JwtAuthFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authspherejwt.exception.JwtAccessDeniedHandler;
import com.authspherejwt.exception.JwtAuthenticationEntryPoint;
import com.authspherejwt.service.CustomUserDetailsService;

/**
 * Security configuration for JWT-based authentication.
 *
 * Responsibilities:
 * - Configure authentication provider (DB-based)
 * - Define secured and public endpoints
 * - Set stateless session policy
 * - Register JWT filter in security chain
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * Constructor-based dependency injection for all security-related components.
     *
     * This constructor wires together all required beans for JWT-based security:
     *
     * @param userDetailsService loads user information from the database for authentication
     * @param jwtAuthFilter intercepts every request to validate JWT tokens
     * @param jwtAuthenticationEntryPoint handles authentication failures (returns 401 Unauthorized)
     * @param jwtAccessDeniedHandler handles authorization failures (returns 403 Forbidden) 
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService,
            JwtAuthFilter jwtAuthFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {
    	this.userDetailsService = userDetailsService;
    	this.jwtAuthFilter = jwtAuthFilter;
    	this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    	this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    /**
     * Configures the security filter chain.
     *
     * Flow:
     * - Disable CSRF (not needed for APIs)
     * - Set stateless session (no session stored on server)
     * - Allow public access to auth endpoints
     * - Secure all other endpoints
     * - Add JWT filter before UsernamePasswordAuthenticationFilter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
	        // Disable CSRF for REST APIs
	        .csrf(csrf -> csrf.disable())
	
	        // Stateless session (JWT based authentication)
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        )
	        
            // Authorization rules (ROLE BASED ACCESS)
	        .authorizeHttpRequests(auth -> auth

	            // Public endpoints
	            .requestMatchers("/api/auth/**").permitAll()

	            // Admin-only endpoints
	            .requestMatchers("/api/admin/**").hasRole("ADMIN")

	            // User endpoints (both USER and ADMIN can access)
	            .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

	            // Any other request must be authenticated
	            .anyRequest().authenticated()
	        )
            
            // Exception handling (401 and 403)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401 handler
                .accessDeniedHandler(jwtAccessDeniedHandler)           // 403 handler
            )

            // Add JWT filter before Spring's authentication filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Authentication provider using database.
     *
     * Uses:
     * - CustomUserDetailsService to load user
     * - BCryptPasswordEncoder to validate password
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /**
     * AuthenticationManager bean required for login API.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Password encoder using BCrypt hashing algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
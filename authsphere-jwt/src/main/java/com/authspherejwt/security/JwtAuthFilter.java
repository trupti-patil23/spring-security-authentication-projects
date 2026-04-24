package com.authspherejwt.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authspherejwt.service.CustomUserDetailsService;

import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthFilter is responsible for validating JWT tokens for every incoming request.
 *
 * Key Responsibilities:
 * - Intercept each HTTP request
 * - Extract JWT token from Authorization header
 * - Validate token using JwtService
 * - Load user details from database
 * - Set authentication in SecurityContext if token is valid
 *
 * This filter ensures stateless authentication for all protected APIs.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * Service to handle JWT operations such as validation and extraction.
     */
    private final JwtService jwtService;

    /**
     * Custom UserDetailsService to load user from database.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor-based dependency injection.
     *
     * @param jwtService handles JWT logic
     * @param userDetailsService loads user details from DB
     */
    public JwtAuthFilter(JwtService jwtService,
                         CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method is executed once per request.
     *
     * Flow:
     * 1. Extract Authorization header
     * 2. Check if it contains Bearer token
     * 3. Extract JWT token
     * 4. Extract username from token
     * 5. Validate token
     * 6. Load user details from DB
     * 7. Create Authentication object
     * 8. Set it in SecurityContext
     * 9. Continue filter chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        /**
         * Step 1: Get Authorization header from request
         * Example: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
         */
        final String authHeader = request.getHeader("Authorization");

        /**
         * Step 2: If header is missing or does not start with "Bearer ",
         * skip JWT processing and continue filter chain.
         */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * Step 3: Extract token by removing "Bearer " prefix
         */
        String token = authHeader.substring(7);

        /**
         * Step 4: Extract username from token
         */
        String username = jwtService.extractUsername(token);

        /**
         * Step 5: Check if username exists and user is not already authenticated
         */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            /**
             * Step 6: Load user details from database
             */
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            /**
             * Step 7: Validate token (username match + expiration check)
             */
            if (jwtService.isTokenValid(token, username)) {

                /**
                 * Step 8: Create authentication token
                 * (credentials = null because password is not needed here)
                 */
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                /**
                 * Step 9: Attach request details (IP, session, etc.)
                 */
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                /**
                 * Step 10: Set authentication in SecurityContext
                 * This tells Spring Security that the user is authenticated
                 */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        /**
         * Step 11: Continue with the next filter in the chain
         */
        filterChain.doFilter(request, response);
    }
}
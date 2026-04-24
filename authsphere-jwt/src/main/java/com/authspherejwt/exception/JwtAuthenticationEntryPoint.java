package com.authspherejwt.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint handles authentication failures.
 *
 * This is triggered when:
 * - User provides invalid credentials
 * - JWT token is missing
 * - JWT token is invalid or expired
 *
 * It returns HTTP 401 Unauthorized response.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * This method is invoked automatically by Spring Security
     * when authentication fails.
     *
     * @param request incoming HTTP request
     * @param response HTTP response object
     * @param authException exception thrown during authentication failure
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Set HTTP status to 401 (Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Response format as JSON
        response.setContentType("application/json");

        String message;
        String authHeader = request.getHeader("Authorization");        

        /**
         * 1. If no token → missing token
         * 2. If token present → invalid/expired token
         */

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Case 1: No token
            message = "Authentication token is missing";

        } else {
            // Case 2: Token present but invalid
            message = "Invalid or expired JWT token";
        }

        String jsonResponse = String.format(
                "{ \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"%s\" }",
                message
        );

        response.getWriter().write(jsonResponse);    
    }
}

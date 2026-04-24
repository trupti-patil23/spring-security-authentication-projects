package com.authspherejwt.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAccessDeniedHandler handles authorization failures.
 *
 * This is triggered when:
 * - User is authenticated (valid JWT)
 * - But tries to access a resource without required role/permission
 *
 * It returns HTTP 403 Forbidden response.
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Handles access denied scenarios.
     *
     * @param request incoming HTTP request
     * @param response HTTP response object
     * @param accessDeniedException exception thrown when access is denied
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        // Set HTTP status to 403 (Forbidden)
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Response format as JSON
        response.setContentType("application/json");

        // Return error message
        response.getWriter().write(
                "{\"error\": \"Forbidden - You do not have permission to access this resource\"}"
        );
    }
}

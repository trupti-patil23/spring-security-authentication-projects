package com.authsphere.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.authsphere.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Security configuration for the application.
 *
 * Handles:
 * - Authentication using database (CustomUserDetailsService)
 * - Authorization using roles (USER, ADMIN)
 * - Session management using Redis
 * - Login, logout, and access denied handling
 */

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	 /**
     * Configures Spring Security filter chain.
     */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http
		 .csrf(csrf -> csrf.disable())   //Disable CSRF for simplicity(should be enabled in production for forms)
	        .authenticationProvider(authProvider())    //Custom authentication provider(DB-based authentication)
	        .authorizeHttpRequests(auth -> auth        // Role-based authorization
	            .requestMatchers("/admin").hasRole("ADMIN")
	            .requestMatchers("/user").hasAnyRole("USER", "ADMIN")
	            .anyRequest().permitAll()
	        )
	        .formLogin(form -> form     // Custom login configuration
	            .loginPage("/login")
	            .successHandler(successHandler()) // login success	        	    
	            .permitAll()
	        ).exceptionHandling(ex -> ex
	            .accessDeniedHandler(accessDeniedHandler())  //Exception handling for 403 (access denied)
	        )
	        .logout(logout -> logout       // Logout configuration
	        	 .logoutUrl("/logout")
	        	 .invalidateHttpSession(true)     // destroy session
	        	 .clearAuthentication(true)       // clear auth
	        	 .deleteCookies("JSESSIONID")     // remove cookie
	        	 .logoutSuccessUrl("/login?logout")
	        	);

	    return http.build();	
	}

	 /**
     * Authentication provider using database.
     * Uses CustomUserDetailsService to load user details.
     */
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService); // your CustomUserDetailsService
		provider.setPasswordEncoder(passwordEncoder()); // your PasswordEncoder bean
		return provider;
	}
	
	 /**
     * Handles access denied (403) scenarios.
     * Returns JSON for API requests and redirects for UI requests.
     */
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
	    return (request, response, ex) -> {

	        String uri = request.getRequestURI();

	        if (uri.startsWith("/api/")) {
	            // JSON response for APIs
	            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            response.setContentType("application/json");

	            response.getWriter().write("""
	            {
	                "status": 403,
	                "error": "Forbidden",
	                "message": "Access Denied"
	            }
	            """);
	        } else {
	            // UI redirect
	            response.sendRedirect("/access-denied");
	        }
	    };
	}
	
	 /**
     * Handles successful login.
     * Redirects user based on role.
     */
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    return (request, response, authentication) -> {

	        var authorities = authentication.getAuthorities();

	        boolean isAdmin = authorities.stream()
	                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

	        if (isAdmin) {
	            response.sendRedirect("/admin");
	        } else {
	            response.sendRedirect("/user");
	        }
	    };
	}

	 /**
     * Password encoder using BCrypt (secure hashing).
     */
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
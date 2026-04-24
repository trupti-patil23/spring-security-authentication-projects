package com.authspherejwt.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.authspherejwt.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


/**
 * JwtService is responsible for handling all JWT-related operations.
 *
 * Responsibilities:
 * - Generate JWT access tokens
 * - Extract username from token
 * - Validate token (signature + expiration) 
 */
@Service
public class JwtService {

    /**
     * Secret key used to sign JWT tokens.
     *      
     */
    private static final String SECRET_KEY = "my-secret-key-my-secret-key-my-secret-key";

    /**
     * Generates JWT token for a given username.
     *
     * @param username the authenticated user's username
     * @return JWT token string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // payload → identifies the user
                .setIssuedAt(new Date(System.currentTimeMillis())) // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // signing algorithm + key
                .compact();
    }

    /**
     * Extracts username (subject) from JWT token.
     *
     * @param token JWT token
     * @return username stored in token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Validates the token against username and expiration.
     *
     * @param token JWT token
     * @param username expected username
     * @return true if valid, false otherwise
     */
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);

        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Checks whether the token has expired.
     *
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Extracts all claims (payload data) from token.
     *
     * @param token JWT token
     * @return Claims object containing token data
     */
    private Claims extractAllClaims(String token) {
    	try {
    	    return Jwts.parserBuilder()
    	            .setSigningKey(getSignKey())
    	            .build()
    	            .parseClaimsJws(token)
    	            .getBody();

    	} catch (ExpiredJwtException e) {
    	    throw new InvalidTokenException("JWT token expired");

    	} catch (JwtException e) {
    	    throw new InvalidTokenException("Invalid JWT token");

    	} catch (Exception e) {
    	    throw new InvalidTokenException("Token processing error");
    	}
    }

    /**
     * Generates signing key from secret.
     *
     * @return Key used for signing and validating JWT
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}

package com.authspherejwt.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entity representing a Refresh Token in the system.
 * 
 * A Refresh Token is a long-lived credential used to obtain new Access Tokens (JWT)
 * without requiring the user to re-authenticate.
 *
 * Unlike JWT access tokens (which are stateless), refresh tokens are stored in the database,
 * making them stateful and allowing explicit session management such as:
 *
 *  -Token expiration control
 *  -Logout (token revocation)
 *  -Session tracking
 *
 * Each refresh token is associated with a single user and represents one active session.
 * 
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    /**
     * Primary key for RefreshToken table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The actual refresh token value.
     *
     * Typically generated as a random UUID string.
     * It is NOT a JWT to allow server-side control and revocation.
     * 
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * Expiration timestamp of the refresh token.
     *
     * After this time, the token becomes invalid and must not be used.   
     */
    @Column(nullable = false)
    private Instant expiryDate;

    /**
     * Associated user for this refresh token.
     *
     * One-to-One relationship:
     * Each user can have one active refresh token 
     *
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // =========================
    // Constructors
    // =========================

    public RefreshToken() {
    }

    public RefreshToken(String token, Instant expiryDate, User user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
package com.authspherejwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * Represents application user.
 * This entity is mapped to the 'users' table in the database.
 */

@Entity
@Table(name = "users")
public class User {
	  /**
     * Primary key for User
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username for login
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Encrypted password (will use BCrypt later)
     */
    @Column(nullable = false)
    private String password;

    /**
     * User role (for future role-based authorization)
     */
    @Column(nullable = false)
    private String role;
    
	public User(String username, String password, String role) {
		super();		
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}    
    
}

package com.authsphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authsphere.entity.User;

/**
 * Repository interface for User entity.
 *
 * Provides database access methods for User operations.
 * Extends JpaRepository for built-in CRUD functionality.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
	 Optional<User> findByUsername(String username);
}

package com.authspherejwt.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.authspherejwt.entity.User;

/**
 * Repository interface for User entity.
 * Provides CRUD operations and custom query methods.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Fetch user by username (used during authentication)
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if username already exists
     */
    boolean existsByUsername(String username);
}

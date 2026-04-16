package com.authsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authsphere.entity.Role;

/**
 * Repository interface for Role entity. *
 * Extends JpaRepository for built-in CRUD functionality. *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}

package com.authspherejwt.controller;

import org.springframework.web.bind.annotation.*;

/**
 * TestController is used to verify JWT authentication
 * and role-based authorization.
 */
@RestController
@RequestMapping("/api")
public class TestController {

    /**
     * Accessible by any authenticated user (USER or ADMIN)
     */
    @GetMapping("/user/profile")
    public String userProfile() {
        return "User profile data - Access granted";
    }

    /**
     * Accessible only by ADMIN role
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Admin dashboard - Access granted";
    }
}
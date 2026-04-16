package com.authsphere.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles navigation between pages (login, user dashboard, admin dashboard, error page).
 * Uses Spring Security for authentication and role-based authorization.
 */

@Controller
public class ViewController {
	
	// Displays custom access denied page (403 error)
	@GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied";
    }
	
	 // Displays login page (Spring Security handles authentication internally)
	@GetMapping("/login")
	public String showLoginPage() {
		return "login"; // Thymeleaf automatically looks for login.html in templates
	}

	 // Displays user dashboard:Accessible by ROLE_USER and ROLE_ADMIN
	@GetMapping("/user")
	public String showUserPage(Model model, Principal principal) {
		model.addAttribute("username", principal.getName());
		return "user";
	}

	// Displays admin dashboard: Accessible only by ROLE_ADMIN
	@GetMapping("/admin")
	public String adminPage(Model model, Principal principal) {		
		model.addAttribute("username", principal.getName());
		return "admin";
	}
}

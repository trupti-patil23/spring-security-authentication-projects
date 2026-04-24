package com.authspherejwt.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authspherejwt.entity.User;
import com.authspherejwt.repository.UserRepository;

/**
 * Custom implementation of UserDetailsService.
 * Loads user-specific data from database for authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Loads user from DB by username
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        // Convert DB role → Spring Security authority
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority) 
        );
    }
}

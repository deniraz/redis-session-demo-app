package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.repo.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
// Marks this class as a Spring-managed service.
// Spring Security will automatically detect it as a UserDetailsService implementation.
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    // Injects the UserRepository so this service can look up users from the database.
    public JpaUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    // This method is called automatically by Spring Security during login.
    // It must return a UserDetails object representing the authenticated user.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Query the database for the user with the given username.
        // If not found, throw UsernameNotFoundException → Spring Security will reject the login.
        UserEntity user = repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Convert the UserEntity into Spring Security's UserDetails object.
        // This includes:
        // - username
        // - password
        // - list of GrantedAuthority (user roles)
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),

            // Convert comma-separated roles string into a list of SimpleGrantedAuthority objects.
            // Example:
            //   "USER,ADMIN" → ["USER", "ADMIN"]
            Arrays.stream(user.getRoles().split(","))
                .map(String::trim)                         // remove spaces
                .map(SimpleGrantedAuthority::new)          // convert each role string into an authority object
                .collect(Collectors.toList())
        );
    }
}

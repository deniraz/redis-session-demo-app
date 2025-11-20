package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// This class provides all Spring Security configuration for the application.
// It defines multiple security filter chains to allow different rules for
// different URL patterns (e.g., actuator vs. main application).
public class SecurityConfig {

    /** 
     * FILTER CHAIN FOR ACTUATOR — FULL PERMISSION
     * This chain handles /actuator/** endpoints and allows them without authentication.
     * It must have the highest priority (Order 0).
     */
    @Bean
    @Order(0)
    public SecurityFilterChain actuatorChain(HttpSecurity http) throws Exception {

        http
            // Selects this chain only when the request matches /actuator/**
            .securityMatcher("/actuator/**")

            // Allows all actuator endpoints without authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()
            )

            // Disable session, CSRF, security context, and request caching
            // because actuator endpoints are stateless and for monitoring use.
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.disable())
            .securityContext(sc -> sc.disable())
            .requestCache(rc -> rc.disable());

        // Builds and returns this filter chain
        return http.build();
    }

    /** MAIN APPLICATION FILTER CHAIN **/
    @Bean
    @Order(1)
    public SecurityFilterChain mainChain(HttpSecurity http) throws Exception {

        http
            // Authorization rules for the main web application
            .authorizeHttpRequests(auth -> auth
                // Secondary actuator allow rule (backup)
                .requestMatchers("/actuator/**").permitAll()

                // Login page must be public
                .requestMatchers("/login").permitAll()

                // Allow static files (CSS, JS, images)
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            // Form-based login configuration
            .formLogin(form -> form
                .loginPage("/login")               // Custom login page
                .defaultSuccessUrl("/", true)      // Redirect after login
                .permitAll()
            )

            // Logout behavior
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

            // Disable CSRF for simplicity in this demo
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    // Password encoder used to hash and verify passwords.
    // BCrypt is the recommended encoder in modern Spring Security setups.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

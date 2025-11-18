package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /** 
     * FILTER CHAIN ACTUATOR — FULL PERMITAN
     * Gunakan requestMatchers, bukan securityMatcher.
     */
    @Bean
    @Order(0)
    public SecurityFilterChain actuatorChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/actuator/**")   // chain selector
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()   // RULE, ini yang penting
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.disable())
            .securityContext(sc -> sc.disable())
            .requestCache(rc -> rc.disable());

        return http.build();
    }

    /** MAIN APP CHAIN **/
    @Bean
    @Order(1)
    public SecurityFilterChain mainChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()  // backup
                .requestMatchers("/login").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

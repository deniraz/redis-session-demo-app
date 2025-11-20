package com.example.demo.setup;

import com.example.demo.model.UserEntity;
import com.example.demo.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
// Marks this class as a Spring-managed component.
// Spring will automatically detect it and execute the run() method at application startup.
public class DataLoader implements CommandLineRunner {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    // Constructor injection for UserRepository and PasswordEncoder.
    // PasswordEncoder is needed to store hashed passwords securely.
    public DataLoader(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    // This method runs automatically right after the Spring Boot application starts.
    // It's commonly used for initial data seeding.
    public void run(String... args) throws Exception {

        // Check if a user named "demo" already exists.
        // If not, create a default demo user for the application.
        if (repo.findByUsername("demo").isEmpty()) {

            UserEntity u = new UserEntity();
            u.setUsername("demo");

            // Encode the plain password "password" using BCryptPasswordEncoder.
            u.setPassword(encoder.encode("password"));

            // Assign the user a basic role.
            // NOTE: Spring Security expects roles to be prefixed with "ROLE_"
            u.setRoles("ROLE_USER");

            // Persist the user into the database.
            repo.save(u);

            System.out.println("✅ Created demo user: demo / password");
        }
    }
}

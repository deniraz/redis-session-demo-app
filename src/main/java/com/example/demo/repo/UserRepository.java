package com.example.demo.repo;

import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Extends JpaRepository to provide built-in CRUD operations for UserEntity:
    //  - save()
    //  - findById()
    //  - findAll()
    //  - delete()
    //  - count()
    //
    // Spring Data JPA will automatically create the implementation at runtime.

    Optional<UserEntity> findByUsername(String username);
    // Custom query method.
    //
    // Spring Data JPA derives the query automatically:
    //   SELECT * FROM users WHERE username = ? LIMIT 1
    //
    // Returns Optional<UserEntity> to cleanly handle cases:
    //   - user exists
    //   - user not found
}

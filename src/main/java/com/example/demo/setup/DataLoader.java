package com.example.demo.setup;

import com.example.demo.model.UserEntity;
import com.example.demo.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
  private final UserRepository repo;
  private final PasswordEncoder encoder;

  public DataLoader(UserRepository repo, PasswordEncoder encoder) {
    this.repo = repo;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) throws Exception {
    if (repo.findByUsername("demo").isEmpty()) {
      UserEntity u = new UserEntity();
      u.setUsername("demo");
      u.setPassword(encoder.encode("password"));
      u.setRoles("ROLE_USER");
      repo.save(u);
      System.out.println("✅ Created demo user: demo / password");
    }
  }
}

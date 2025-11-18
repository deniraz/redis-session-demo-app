package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.repo.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {
  private final UserRepository repo;

  public JpaUserDetailsService(UserRepository repo) {
    this.repo = repo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = repo.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      Arrays.stream(user.getRoles().split(","))
        .map(String::trim)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList())
    );
  }
}

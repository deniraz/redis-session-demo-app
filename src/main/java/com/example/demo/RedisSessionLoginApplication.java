package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Marks this class as the main entry point of the Spring Boot application.
// It enables:
// 1) Component scanning (for controllers, services, configs, etc.)
// 2) Auto-configuration
// 3) Spring Boot application bootstrap process
public class RedisSessionLoginApplication {

    public static void main(String[] args) {
        // Starts the Spring Boot application.
        // This method:
//  - Initializes the Spring IoC container
//  - Loads all beans (@Configuration, @Service, @Controller, @Entity, etc.)
//  - Starts the embedded Tomcat web server
        SpringApplication.run(RedisSessionLoginApplication.class, args);
    }
}

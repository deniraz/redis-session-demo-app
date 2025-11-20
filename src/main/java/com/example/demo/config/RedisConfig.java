package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
// Marks this class as a Spring configuration provider.
// Spring will detect this class and register any @Bean methods inside it.
public class RedisConfig {

    @Bean
    // Exposes a StringRedisTemplate bean to the Spring context.
    // StringRedisTemplate is a Redis helper used for interacting with Redis using String keys and values.
    // It provides simple operations such as GET, SET, EXPIRE, DELETE, etc.
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {

        // Creates a new StringRedisTemplate using the application's RedisConnectionFactory.
        // The connectionFactory (auto-configured by Spring Boot) knows how to connect
        // to the Redis server defined via application configuration or environment variables.
        return new StringRedisTemplate(connectionFactory);
    }
}

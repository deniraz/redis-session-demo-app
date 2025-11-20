package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
// Marks this class as a Spring configuration source.
// Even though the class is empty, the annotations applied to it have important effects.
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
// Enables Spring Session backed by Redis.
//
// This annotation replaces the default servlet container (Tomcat) session storage.
// Key effects:
//   • HTTP session data is stored in Redis instead of application memory.
//   • Allows seamless session sharing across multiple EC2 instances behind an ALB.
//   • "maxInactiveIntervalInSeconds = 1800" sets session timeout to 30 minutes.
//
// Without this annotation, each instance would store sessions locally
// and users would be logged out whenever the ALB routes them to a different instance.
public class SessionConfig {
    // Empty class — all functionality is provided by the annotations above.
}

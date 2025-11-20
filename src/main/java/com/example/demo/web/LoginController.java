package com.example.demo.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@Controller
// Marks this class as a Spring MVC controller.
// Methods returning String will resolve to Thymeleaf templates (login.html, home.html).
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;  
    // Injects the Redis template used to interact with Redis.
    // In this demo, it is used to read the TTL of the session key
    // so we can display session expiration info on the UI.

    @GetMapping("/login")
    // Handles GET requests to the login page.
    // Simply returns the login.html template.
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    // Main home page after successful login.
    // Displays:
    // - Session ID
    // - Which EC2 instance served the request
    // - The Redis key storing the session
    // - TTL remaining for the session
    // - Session creation/access timestamps
    //
    // This makes it easy to demonstrate session sharing across EC2 instances.
    public String home(Model model, HttpSession session) throws Exception {

        // Extract the current session ID.
        String sessionId = session.getId();

        // Get current EC2 instance hostname.
        // Useful to show which instance served this request.
        String host = InetAddress.getLocalHost().getHostName();

        // Construct the Redis session key used by Spring Session.
        // Spring stores session data under:
        // spring:session:sessions:<sessionId>
        String redisKey = "spring:session:sessions:" + sessionId;

        // Read the TTL (Time To Live) for the session key in Redis.
        // TTL is how long the session will remain active before expiring.
        Long ttlSeconds = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);

        // Session metadata from HttpSession
        long creationTime = session.getCreationTime();
        long lastAccessedTime = session.getLastAccessedTime();

        // Pass all data to the Thymeleaf template (home.html)
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("host", host);
        model.addAttribute("redisKey", redisKey);
        model.addAttribute("ttl", ttlSeconds);
        model.addAttribute("creationTime", creationTime);
        model.addAttribute("lastAccessedTime", lastAccessedTime);

        // Render home.html
        return "home";
    }
}

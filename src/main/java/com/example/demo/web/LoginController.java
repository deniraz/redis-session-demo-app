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
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) throws Exception {

        String sessionId = session.getId();
        String host = InetAddress.getLocalHost().getHostName();

        // Redis session key
        String redisKey = "spring:session:sessions:" + sessionId;

        // TTL in seconds
        Long ttlSeconds = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);

        long creationTime = session.getCreationTime();
        long lastAccessedTime = session.getLastAccessedTime();

        model.addAttribute("sessionId", sessionId);
        model.addAttribute("host", host);
        model.addAttribute("redisKey", redisKey);
        model.addAttribute("ttl", ttlSeconds);
        model.addAttribute("creationTime", creationTime);
        model.addAttribute("lastAccessedTime", lastAccessedTime);

        return "home";
    }
}

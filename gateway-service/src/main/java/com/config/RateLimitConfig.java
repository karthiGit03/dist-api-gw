package com.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RateLimitConfig {

    private final Map<String, Integer> limits = new HashMap<>();

    public RateLimitConfig() {

        limits.put("/login", 5);

        limits.put("/search", 10);

        limits.put("/admin", 100);
    }

    public int getLimit(String endpoint) {

        return limits.getOrDefault(endpoint, 5);
    }
}
package com.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.config.RateLimitConfig;

@Service
public class RateLimiterService {

    private static final int WINDOW_SIZE = 60;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RateLimitConfig rateLimitConfig;

    public boolean allowRequest(String ipAddress,
                                String endpoint) {

        int maxRequests =
                rateLimitConfig.getLimit(endpoint);

        String key =
                "rate_limit:" + ipAddress + ":" + endpoint;

        Long requests =
                redisTemplate.opsForValue().increment(key);

        if (requests == 1) {

            redisTemplate.expire(
                    key,
                    WINDOW_SIZE,
                    TimeUnit.SECONDS
            );
        }

        return requests <= maxRequests;
    }
}
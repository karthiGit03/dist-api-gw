package com.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SlidingWindowService {

    private static final int MAX_REQUESTS = 5;

    private static final int WINDOW_SIZE = 60;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean allowRequest(String ipAddress,
                                String endpoint) {

        String key =
                "sliding_window:"
                + ipAddress
                + ":"
                + endpoint;

        long currentTime =
                System.currentTimeMillis();

        long windowStart =
                currentTime -
                (WINDOW_SIZE * 1000);

        redisTemplate.opsForZSet()
                .removeRangeByScore(
                        key,
                        0,
                        windowStart
                );

        Long requestCount =
                redisTemplate.opsForZSet()
                        .zCard(key);

        if (requestCount >= MAX_REQUESTS) {
            return false;
        }

        redisTemplate.opsForZSet()
                .add(
                        key,
                        String.valueOf(currentTime),
                        currentTime
                );

        redisTemplate.expire(
                key,
                WINDOW_SIZE,
                TimeUnit.SECONDS
        );

        return true;
    }
}
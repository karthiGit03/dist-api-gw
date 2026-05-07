package com.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import com.model.RateLimitResponse;

@Service
public class TokenBucketService {

    public static final int CAPACITY = 5;

    // 5 tokens every 60 seconds
    private static final int REFILL_RATE = 5;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public RateLimitResponse allowRequest(
            String ipAddress,
            String endpoint) {

        String key =
                "token_bucket:"
                + ipAddress
                + ":"
                + endpoint;

        long currentTime =
                System.currentTimeMillis() / 1000;
  
        String luaScript = """
                local tokens_key = KEYS[1]
                local timestamp_key = KEYS[2]

                local capacity = tonumber(ARGV[1])
                local refill_rate = tonumber(ARGV[2])
                local current_time = tonumber(ARGV[3])

                local tokens =
                    tonumber(redis.call("GET", tokens_key))

                if tokens == nil then
                    tokens = capacity
                end

                local last_refill =
                    tonumber(redis.call("GET", timestamp_key))

                if last_refill == nil then
                    last_refill = current_time
                end

                local elapsed =
                    current_time - last_refill

                local refill =
                    math.floor(elapsed / 60)
                    * refill_rate

                tokens =
                    math.min(
                        capacity,
                        tokens + refill
                    )

                if refill > 0 then
                    last_refill = current_time
                end

                if tokens < 1 then

                    redis.call(
                        "SET",
                        tokens_key,
                        tokens
                    )

                    redis.call(
                        "SET",
                        timestamp_key,
                        last_refill
                    )

                    return 0
                end

                tokens = tokens - 1

                redis.call(
                    "SET",
                    tokens_key,
                    tokens
                )

                redis.call(
                    "SET",
                    timestamp_key,
                    last_refill
                )

                return 1
                """;

        DefaultRedisScript<Long> script =
                new DefaultRedisScript<>();

        script.setScriptText(luaScript);

        script.setResultType(Long.class);

        Long result =
                stringRedisTemplate.execute(
                        script,
                        Arrays.asList(
                                key + ":tokens",
                                key + ":timestamp"
                        ),
                        String.valueOf(CAPACITY),
                        String.valueOf(REFILL_RATE),
                        String.valueOf(currentTime)
                );

        String tokens =
                stringRedisTemplate.opsForValue()
                        .get(key + ":tokens");

        long remaining =
                tokens == null
                ? 0
                : Long.parseLong(tokens);

        String timestamp =
                stringRedisTemplate.opsForValue()
                        .get(key + ":timestamp");

        long refillTime = 60;

        if (timestamp != null) {

            long lastRefill =
                    Long.parseLong(timestamp);

            long elapsed =
                    currentTime - lastRefill;

            refillTime =
                    Math.max(0, 60 - elapsed);
        }

        return new RateLimitResponse(
                result == 1,
                remaining,
                refillTime
        );
    }
}
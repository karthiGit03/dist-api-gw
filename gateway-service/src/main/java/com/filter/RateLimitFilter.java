package com.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.model.RateLimitResponse;
import com.service.TokenBucketService;

import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter
        implements GlobalFilter, Ordered {

    @Autowired
    private TokenBucketService tokenBucketService;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String ipAddress =
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress();

        String endpoint =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        RateLimitResponse response =
                tokenBucketService.allowRequest(
                        ipAddress,
                        endpoint
                );

        exchange.getResponse()
                .getHeaders()
                .add(
                        "X-RateLimit-Limit",
                        String.valueOf(
                                TokenBucketService.CAPACITY
                        )
                );

        exchange.getResponse()
                .getHeaders()
                .add(
                        "X-RateLimit-Remaining",
                        String.valueOf(
                                response.getRemainingTokens()
                        )
                );
        
        exchange.getResponse()
        .getHeaders()
        .add(
                "X-RateLimit-Refill-Time",
                String.valueOf(
                        response.getRefillTime()
                )
        );

        if (!response.isAllowed()) {

            exchange.getResponse()
                    .getHeaders()
                    .add(
                            "Retry-After",
                            "60"
                    );

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.TOO_MANY_REQUESTS
                    );

            return exchange.getResponse()
                    .setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
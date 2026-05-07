# Distributed API Gateway with Redis Rate Limiting

A scalable distributed backend with centralized API routing, token bucket rate limiting, and horizontal scaling.

**Stack:** Java 17, Spring Boot 3, Spring Cloud Gateway, Spring WebFlux, Redis, Docker

---

### Features

- Centralized API routing via Spring Cloud Gateway
- Distributed token bucket rate limiting using Redis + Lua scripts
- Load balancing across multiple backend instances
- Custom response headers for quota tracking
- Reactive request filtering with WebFlux

---

### Rate Limits

| Endpoint | Limit |
|----------|-------|
| `/login` | 5 req/min |
| `/search` | 20 req/min |
| `/payment` | 2 req/min |

Response headers on every request:

```
X-RateLimit-Limit: 5
X-RateLimit-Remaining: 3
X-RateLimit-Refill-Time: 42
Retry-After: 60
```

---

### Quick Start

```bash
# Start Redis
docker run --name redis -p 6379:6379 redis

# Run backend instances on ports 9091, 9092, 9093
# Run gateway on port 6060

# Test
GET http://localhost:6060/login
```

---

### Roadmap

- JWT Authentication & role-based rate limits
- Circuit Breaker Pattern
- Prometheus + Grafana Monitoring
- Kubernetes Deployment
- Eureka Service Discovery
- Distributed Tracing

---

Built by **Karthikeyan Sankar 😎**

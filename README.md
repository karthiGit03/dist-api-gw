Distributed API Gateway with Redis Rate Limiting
A scalable distributed backend built with Spring Boot, Spring Cloud Gateway, Redis, and Docker — featuring centralized routing, token bucket rate limiting, and horizontal scaling.

Architecture
Client → Gateway (:6060) → RateLimitFilter → Redis Token Bucket → Load Balancer → Backend Instances (:9091 / :9092 / :9093)
Tech Stack
LayerToolsBackendJava 17, Spring Boot 3, Spring WebFluxGatewaySpring Cloud Gateway, Spring Cloud LoadBalancerRate LimitingRedis, Lua ScriptingDevOpsDocker, Docker Compose
Rate Limiting
Token Bucket algorithm backed by atomic Redis Lua scripts.
EndpointLimit/login5 req/min/search20 req/min/payment2 req/min
Every response includes quota headers:
X-RateLimit-Limit: 5
X-RateLimit-Remaining: 3
X-RateLimit-Refill-Time: 42
Retry-After: 60
Quick Start
bash# Start Redis
docker run --name redis -p 6379:6379 redis

# Run backend instances on ports 9091, 9092, 9093
# Run gateway on port 6060

# Test
GET http://localhost:6060/login
Project Structure
gateway-service/
├── filter/        # RateLimitFilter (GlobalFilter)
├── service/       # TokenBucketService
├── config/        # LoadBalancerConfig
└── model/         # RateLimitResponse
Key Concepts
Distributed Rate Limiting · Token Bucket Algorithm · Reactive Programming · API Gateway Pattern · Horizontal Scaling · Atomic Redis Operations
Roadmap

 JWT Authentication & role-based rate limits
 Circuit Breaker Pattern
 Prometheus + Grafana Monitoring
 Kubernetes Deployment
 Eureka Service Discovery
 Distributed Tracing


Built by Karthikeyan Sankar · Demonstrates distributed systems, reactive backend design, and cloud-native architecture.

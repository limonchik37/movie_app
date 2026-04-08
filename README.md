# Movie Review Platform — Microservices

A movie review platform built with a microservices architecture using **Kotlin**, **Spring Boot 3**, **Apache Kafka**, and **Docker**. Users can register, browse movies, and post reviews with image attachments. Services communicate asynchronously via Kafka events.

---

## Architecture

```
                        ┌─────────────────┐
  Android / Browser ───▶│   API Gateway   │ :8080
                        │ (Spring Cloud)  │
                        └────────┬────────┘
                                 │ routes
            ┌────────────────────┼────────────────────┐
            ▼                    ▼                    ▼
   ┌─────────────────┐  ┌───────────────┐  ┌──────────────────┐
   │  user-service   │  │ movie-service │  │ review-service   │
   │  :8081          │  │ :8082         │  │ :8083            │
   │  Auth + Users   │  │ Movie CRUD    │  │ Review CRUD      │
   │  JWT (HS512)    │  │               │  │ Kafka Producer   │
   └────────┬────────┘  └───────────────┘  └────────┬─────────┘
            │ user.registered                        │ review.created
            └──────────────────┬─────────────────────┘
                               ▼
                   ┌───────────────────────┐
                   │  notification-service  │
                   │  :8084                 │
                   │  Kafka Consumer        │
                   │  Email Notifications   │
                   └───────────────────────┘
```

### Kafka Topics

| Topic              | Producer       | Consumer              | Payload                                      |
|--------------------|----------------|-----------------------|----------------------------------------------|
| `review.created`   | review-service | notification-service  | reviewId, movieId, userId, text, createdAt   |
| `review.deleted`   | review-service | notification-service  | reviewId, movieId, userId                    |
| `user.registered`  | user-service   | notification-service  | userId, username, registeredAt               |

---

## Services

| Service                | Port | Responsibility                                   | Database   |
|------------------------|------|--------------------------------------------------|------------|
| `api-gateway`          | 8080 | Routing, CORS                                    | —          |
| `user-service`         | 8081 | Registration, login, JWT, user profile CRUD      | `user_db`  |
| `movie-service`        | 8082 | Movie CRUD                                       | `movie_db` |
| `review-service`       | 8083 | Review CRUD, Kafka producer                      | `review_db`|
| `notification-service` | 8084 | Kafka consumer, email notifications (Spring Mail)| —          |

---

## Tech Stack

- **Language:** Kotlin 1.9 / JVM 21
- **Framework:** Spring Boot 3.4.4
- **Gateway:** Spring Cloud Gateway
- **Message Broker:** Apache Kafka (Confluent 7.6)
- **Database:** PostgreSQL 16 (separate DB per service)
- **Authentication:** JWT (HS512) + Spring Security
- **Containerization:** Docker + Docker Compose
- **Android Client:** Kotlin, Jetpack Compose, Retrofit2, MVVM

---

## Getting Started

### Prerequisites

- Docker & Docker Compose
- JDK 21 (for local development)

### Run with Docker Compose

```bash
cd microservices
docker-compose up --build
```

All services, Kafka, Zookeeper, and PostgreSQL will start automatically.

### Run a single service locally

```bash
cd user-service
gradle bootRun
```

> Make sure PostgreSQL and Kafka are running (via Docker Compose) before starting a service locally.

---

## API Reference

All requests go through the **API Gateway** at `http://localhost:8080`.

### Auth

```
POST /api/auth/register    { "login": "alice", "password": "secret" }
POST /api/auth/login       { "login": "alice", "password": "secret" }  → { token, userId, username }
```

### Users

```
GET    /users          → List all users
GET    /users/{id}     → Get user by ID
PUT    /users/{id}     → Update profile      🔒 JWT required
DELETE /users/{id}     → Delete user         🔒 JWT required
```

### Movies

```
GET    /movies          → List all movies
GET    /movies/{id}     → Get movie by ID
POST   /movies          → Create movie       🔒 JWT required
PUT    /movies/{id}     → Update movie       🔒 JWT required
DELETE /movies/{id}     → Delete movie       🔒 JWT required
```

### Reviews

```
GET    /movies/{movieId}/reviews              → List reviews for a movie
GET    /reviews/{id}                          → Get single review
POST   /movies/{movieId}/reviews              → Create review    🔒 JWT required
PUT    /movies/{movieId}/reviews/{reviewId}   → Update review    🔒 JWT required
DELETE /movies/{movieId}/reviews/{reviewId}   → Delete review    🔒 JWT required
```

### Authentication

Protected endpoints require the JWT token in the header:

```
Authorization: Bearer <token>
```

---

## Project Structure

```
microservices/
├── docker-compose.yml
├── init-db.sql                    # Creates user_db, movie_db, review_db
├── api-gateway/
│   └── src/main/resources/application.yml
├── user-service/
│   └── src/main/kotlin/.../
│       ├── controller/            # AuthController, UserController
│       ├── service/               # UserService (register, login, CRUD)
│       ├── security/              # JwtUtils, JwtAuthFilter, SecurityConfig
│       ├── kafka/                 # UserEventProducer, UserRegisteredEvent
│       ├── model/                 # User entity
│       └── repository/
├── movie-service/
│   └── src/main/kotlin/.../
│       ├── controller/            # MovieController
│       ├── service/               # MovieService (CRUD)
│       ├── security/              # JwtAuthFilter, SecurityConfig
│       ├── model/                 # Movie entity
│       └── repository/
├── review-service/
│   └── src/main/kotlin/.../
│       ├── controller/            # ReviewController
│       ├── service/               # ReviewService (CRUD + Kafka produce)
│       ├── security/              # JwtAuthFilter, SecurityConfig
│       ├── kafka/                 # ReviewEventProducer, events
│       ├── model/                 # Review entity
│       └── repository/
└── notification-service/
    └── src/main/kotlin/.../
        └── kafka/
            ├── NotificationConsumer.kt    # @KafkaListener methods
            ├── KafkaConsumerConfig.kt     # Consumer factory beans
            └── KafkaEvents.kt             # Shared event data classes
```

---

## Environment Variables

Each service is configured via environment variables (with defaults for local dev):

| Variable                        | Default                            | Used by                          |
|---------------------------------|------------------------------------|----------------------------------|
| `SPRING_DATASOURCE_URL`         | `jdbc:postgresql://localhost:5432/…`| user, movie, review              |
| `SPRING_DATASOURCE_USERNAME`    | `postgres`                         | user, movie, review              |
| `SPRING_DATASOURCE_PASSWORD`    | `123456789`                        | user, movie, review              |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS`| `localhost:9092`                   | user, review, notification       |
| `JWT_SECRET`                    | *(base64 key)*                     | user, movie, review              |
| `JWT_EXPIRATION_MS`             | `3600000` (1 hour)                 | user-service                     |
| `MOVIE_SERVICE_URL`             | `http://localhost:8082`            | api-gateway, review-service      |
| `USER_SERVICE_URL`              | `http://localhost:8081`            | api-gateway                      |
| `REVIEW_SERVICE_URL`            | `http://localhost:8083`            | api-gateway                      |
| `MAIL_HOST`                     | `smtp.gmail.com`                   | notification-service             |
| `MAIL_USERNAME`                 | `noreply@movieapp.com`             | notification-service             |
| `MAIL_PASSWORD`                 | `changeme`                         | notification-service             |

---

## Original Monolith

The original monolithic Spring Boot application is preserved in the `/api` folder and remains fully functional. The `/microservices` folder contains the refactored version.

---

## Android Client

The Android app (Jetpack Compose) lives in `/frontend`. It connects to the API Gateway at `http://10.0.2.2:8080` (Android emulator localhost mapping) and uses Retrofit2 with an OkHttp JWT interceptor.

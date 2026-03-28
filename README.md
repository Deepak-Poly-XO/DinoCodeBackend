# DinoCode — Backend

Spring Boot backend for DinoCode, a real-time code collaboration platform.

**Frontend:** https://dino-code-frontend.vercel.app

---

## What it does

Handles user authentication, session management, code persistence, and real-time WebSocket communication. Every request to a protected endpoint requires a valid JWT token. Code is saved to PostgreSQL and survives server restarts.

---

## Tech Stack

| Layer | Tech |
|-------|------|
| Framework | Spring Boot 3.4.2 |
| Language | Java 17 |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Database | PostgreSQL + Spring Data JPA + Hibernate |
| WebSockets | Spring WebSocket (raw handler) |
| Password Hashing | BCrypt |
| Deployment | Docker on Render |

---

## Architecture
```
Request
   ↓
JwtFilter          — validates Bearer token on every request
   ↓
SecurityConfig     — route rules (public vs protected)
   ↓
Controller         — handles HTTP endpoints
   ↓
Service            — business logic
   ↓
Repository         — JPA queries to PostgreSQL
```

---

## API Endpoints

### Auth
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/auth/register` | Public | Register new user |
| POST | `/auth/login` | Public | Login, returns JWT token |

### Sessions
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/sessions?name=x` | Required | Create a new session |
| GET | `/sessions` | Required | Get all active sessions |
| GET | `/sessions/{id}` | Required | Get session by ID |
| GET | `/sessions/my` | Required | Get sessions created by logged in user |
| PUT | `/sessions/{id}/code` | Required | Save code for a session |

### System
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/health` | Public | Health check for uptime monitoring |

### WebSocket
| Endpoint | Description |
|----------|-------------|
| `/ws/{sessionId}?user={username}` | Join a real-time session |

WebSocket messages use structured JSON:
```json
{ "type": "CODE",  "content": "..." }
{ "type": "JOIN",  "user": "deepak"  }
{ "type": "LEAVE", "user": "deepak"  }
```

---

## Project Structure
```
src/main/java/com/realtimecode/
├── config/
│   └── WebSocketConfig.java          # WebSocket handler registration
├── controller/
│   ├── AuthController.java           # /auth/register + /auth/login
│   ├── SessionController.java        # Session CRUD + code saving
│   └── HealthController.java         # /health endpoint
├── handler/
│   └── CodeEditorWebSocketHandler.java  # Real-time sync + presence events
├── model/
│   ├── User.java                     # JPA entity — users table
│   └── Session.java                  # JPA entity — sessions table
├── repository/
│   ├── UserRepository.java           # findByEmail()
│   └── SessionRepository.java        # findByCreatedBy()
├── security/
│   ├── JwtUtil.java                  # Token generation + validation
│   ├── JwtFilter.java                # Request interceptor
│   └── CustomUserDetailsService.java # Loads user for Spring Security
└── service/
    └── SessionService.java           # Session business logic
```

---

## Getting Started

### Prerequisites
- Java 17+
- PostgreSQL database
- Maven (or use the included `mvnw` wrapper)

### Setup
```bash
git clone https://github.com/Deepak-Poly-XO/DinoCodeBackend
cd DinoCodeBackend
```

Create `src/main/resources/application.properties`:
```properties
spring.application.name=demo

spring.datasource.url=jdbc:postgresql://YOUR_HOST/YOUR_DB
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
```
```bash
./mvnw spring-boot:run
```

Backend runs at `http://localhost:8080`

---

## Environment Variables (Production)

Set these on Render under Environment Variables:

| Variable | Description |
|----------|-------------|
| `DB_URL` | `jdbc:postgresql://host/dbname` |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for signing JWT tokens |

---

## Deployment

Deployed via Docker on Render. The `Dockerfile` in the root handles the build:
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve
COPY src ./src
RUN ./mvnw clean install -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
```

---

## Security Notes

- Passwords are hashed with BCrypt — never stored as plain text
- JWT tokens are signed with a secret key stored as an environment variable
- All protected routes require a valid Bearer token
- CORS is configured to only allow requests from the frontend origin
- Credentials are never committed to version control

---

## Frontend

The frontend repo is here: https://github.com/Deepak-Poly-XO/DinoCodeFrontend

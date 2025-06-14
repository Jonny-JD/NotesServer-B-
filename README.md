# Notes Backend

This is a backend service for my personal note-taking web application project. It provides a REST API to create, read, update, and delete notes, with support for public and private visibility, and session-based authentication.

---

## Technologies

- Java 21  
- Spring Boot (Web, Security, Data JPA, Liquibase)  
- PostgreSQL (external database)  
- Docker (for backend container)  
- Testcontainers (integration tests)  
- JUnit, Mockito (unit tests)

---

## About

This is a pet project I built by myself to deepen my backend development skills using Java and Spring. The backend handles user registration, login with session cookies, and managing notes. Notes can be either private or public, and users can browse notes shared publicly by others.

Database migrations are handled via Liquibase. The backend is designed to be stateless except for sessions managed by Spring Security.

---

## Setup

### Configuration

The app requires a PostgreSQL database running separately. Database connection details and other configs are provided via environment variables:

- `DB_URL` — JDBC URL to PostgreSQL  
- `DB_USERNAME` — DB username  
- `DB_PASSWORD` — DB password  
- `ALLOWED_ORIGINS` — for CORS (e.g., frontend URL)

Example `application.yml` snippet:

spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.yaml

app:
  cors:
    allowed-origins: ${ALLOWED_ORIGINS}

### Running locally

1. Start PostgreSQL (outside of Docker).  
2. Set environment variables for DB connection and CORS.  
3. Run via Gradle:

./gradlew bootRun

### Running with Docker

The backend container expects external DB connection:

docker build -t notes-backend .

docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host:5432/notesdb \
  -e DB_USERNAME=notesuser \
  -e DB_PASSWORD=notespassword \
  -e ALLOWED_ORIGINS=http://localhost:3000 \
  notes-backend

---

## API Overview

- Register, login, logout users with session cookies  
- CRUD notes with private/public flag  
- List, search, and filter notes  

---

## Testing

Unit tests with JUnit/Mockito, integration tests use Testcontainers with PostgreSQL.

Run tests:

./gradlew test

---

## Notes

This project helped me learn how to build RESTful APIs with Spring, handle session-based auth, database migrations, and write tests. It’s part of my portfolio showcasing backend skills.

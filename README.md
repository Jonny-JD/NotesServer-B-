# Notes — Backend

Backend part of the **Notes** full-stack pet project — a web application for creating and browsing public and private notes.

## Tech Stack

- **Java 21** + **Spring Boot** (Web, Security, Data JPA)
- **PostgreSQL** + **Liquibase** (migrations) + **Hibernate**
- **JUnit** + **Mockito** + **Testcontainers** (testing)
- **Gradle** (build tool)
- **Docker**

## Features

- User registration and login with session-based authentication
- CRUD operations for notes (private and public)
- Viewing other users' public notes
- REST API with filtering and pagination

## CI/CD Architecture

The project follows a **GitOps approach**: this repository is responsible only for building and publishing the Docker image. Deployment is handled by a separate infrastructure repository.

```
Push to branch
      │
      ▼
GitHub Actions
  ├── ./gradlew clean build         (produces JAR)
  ├── docker build --build-arg JAR_FILE=...
  ├── docker push → Docker Hub
  └── repository_dispatch → NotesServer-Infrastructure
                                     │
                                     ▼
                             Deploy to Kubernetes
                             (bare metal cluster)
```

### Environments

| Branch   | Image tag       | Environment |
|----------|-----------------|-------------|
| `master` | `{sha}`         | Production  |
| `feature`| `{sha}-feature` | Feature     |

Images are always tagged by commit SHA — no `latest` tag, every deployment is reproducible.

## Local Setup

Make sure PostgreSQL is running, then:

```bash
./gradlew clean build
docker build -t notes-backend --build-arg JAR_FILE=$(ls build/libs/*.jar | grep -v plain) .
docker run -p 8080:8080 notes-backend
```

## Tests

- **Unit tests** — JUnit + Mockito
- **Integration tests** — Testcontainers + PostgreSQL (no external DB required)

```bash
./gradlew test
```

## About

This project was built for my portfolio. It demonstrates:
- Building a REST API with Spring Boot and Spring Security from scratch
- Session-based authentication and role-based access control
- Database migrations with Liquibase
- Integration testing with Testcontainers
- Containerization with a multi-stage-friendly Gradle + Docker build
- CI/CD pipeline with separated build and deploy stages
- Integration with a Kubernetes-based infrastructure

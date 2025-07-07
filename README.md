# 🧠 CyberNotes — Fullstack Note-Taking Web Application

## 📌 About the Project
CyberNotes is a pet project developed from scratch by me. It is a fully functional application for creating, storing, and searching both public and private notes. Modern technologies are used both on the backend and frontend.

## 🛠️ Technology Stack

### Backend:
- Java 21, Spring Boot (Web, Security, Data JPA)
- Liquibase, PostgreSQL
- Docker
- JUnit, Mockito, Testcontainers

### Frontend:
- React + Vite
- TypeScript
- React Router
- LESS
- Nginx (production)

### DevOps:
- Docker Compose
- GitHub Actions (CI/CD)
- Deployment on VPS

## 🚀 Features
- User registration and login with session-based authentication
- CRUD operations for notes (private and public)
- Viewing other users’ public notes
- REST API with filtering and pagination
- Clear separation between backend and frontend
- CI/CD pipeline and deployment (nginx + Docker)

## 🌐 Demo
⚠️ Currently running via ngrok, so the link might be unstable:

▶️ https://goat-trusting-amazingly.ngrok-free.app

## 🧩 Repositories
- Backend: [github.com/Jonny-JD/NotesServer-B-](https://github.com/Jonny-JD/NotesServer-B-)
- Frontend: [github.com/Jonny-JD/NotesServer-F-](https://github.com/Jonny-JD/NotesServer-F-)

## ⚙️ Quick Start

```bash
# Backend
git clone https://github.com/Jonny-JD/NotesServer-B-.git
cd cybernotes-backend
docker-compose up --build

# Frontend (in another terminal)
git clone https://github.com/Jonny-JDNotesServer-F-.git
cd cybernotes-frontend
npm install
npm run dev
```
## 🧪 Tests
- Unit tests (JUnit + Mockito)
- Integration tests (Testcontainers + PostgreSQL)

```bash
./gradlew test
```

## 👤 Author
Sergey Izotov
- 📧 jonny.cbrigante@gmail.com
- 🔗 [LinkedIn](https://www.linkedin.com/in/sergei-izotov-0740a3a5/)

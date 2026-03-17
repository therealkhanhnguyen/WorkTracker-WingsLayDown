# 🛠️ WorkTracker – Full-Stack Job Tracking System

WorkTracker is a full-stack web application designed to track manufacturing job progress across multiple wing sections. The system provides real-time visibility into job status, helping leadership and teams monitor workflows and improve efficiency on the shop floor.

---

## 🚀 Features

- Track job progress across multiple wing sections
- Visualize job status through an interactive UI
- Role-based access control (Manager, Employee, QA)
- Structured job lifecycle workflow:
  - CREATED → IN_WORK → READY_FOR_INSPECTION → READY_FOR_FINAL → COMPLETED
- RESTful API for managing jobs, users, and workflows
- Real-time data persistence using PostgreSQL

---

## 🧱 Tech Stack

### Backend
- Java
- Spring Boot
- Spring Data JPA / Hibernate
- REST API

### Frontend
- Angular
- TypeScript
- HTML / CSS

### Database
- PostgreSQL

### DevOps / Tools
- Docker
- AWS (Deployment)
- Git / GitHub

---

## 🏗️ System Architecture

- Frontend (Angular) communicates with backend via REST APIs
- Backend (Spring Boot) handles business logic and data processing
- PostgreSQL stores job, user, and workflow data
- Docker used for containerization and deployment

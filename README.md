# 📇 Smart Contact Manager

[![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://java.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white)](https://spring.io)
[![JWT](https://img.shields.io/badge/JWT-black?style=flat&logo=JSON%20web%20tokens)](https://jwt.io)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)](https://docker.com)

A secure, full-featured contact management system with JWT-based authentication, cloud-ready deployment, and complete CRUD operations.

## ✨ Features
- 🔑 JWT-based stateless authentication
- 🛡️ Spring Security for endpoint protection
- 📋 Full CRUD operations for contacts
- ✅ Input validation & error handling
- 🐳 Dockerized for AWS cloud deployment
- 🧪 API tested via Postman

## 🛠️ Tech Stack
| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.x |
| Auth | JWT + Spring Security |
| Database | MySQL + Spring Data JPA |
| Testing | Postman |
| DevOps | Docker, AWS EC2 |

## 🚀 Getting Started
```bash
# Clone
git clone https://github.com/AnshulRathore1020/Smart-Contact-Manager

# Set environment variables
JWT_SECRET=your_secret_key
DB_URL=jdbc:mysql://localhost:3306/contactdb

# Run
mvn spring-boot:run
```

## 📡 API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register user |
| POST | `/api/auth/login` | Login → returns JWT |
| GET | `/api/contacts` | Get all contacts |
| POST | `/api/contacts` | Create contact |
| PUT | `/api/contacts/{id}` | Update contact |
| DELETE | `/api/contacts/{id}` | Delete contact |

## 🤝 Connect
Made with ❤️ by [Anshul Rathore](https://www.linkedin.com/in/anshul-rathore-it/)

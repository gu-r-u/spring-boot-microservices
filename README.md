🚀 Spring Boot Microservices Architecture










This repository demonstrates a real-world microservices architecture built using Spring Boot and Spring Cloud.

The project is designed as a learning-driven portfolio project, evolving step by step from core microservices concepts to production-ready architectural patterns used in real backend systems.

🧠 Architecture Overview

The system follows an API Gateway–based microservices architecture, where each service is independent, loosely coupled, and scalable.

Core Principles

Independent Spring Boot applications

Database per service

REST-based inter-service communication

Centralized entry point using API Gateway

Service discovery using Eureka

Independent deployment and scalability

High-Level Flow
               +-------------------+
               |   Eureka Server   |
               | (Service Registry)|
               +---------+---------+
                         |
        ---------------------------------------------
        |                                           |
+-------------------+                   +-------------------+
|   User Service    |                   |  Order Service   |
|   (MySQL DB)      |                   |  (MySQL DB)      |
+-------------------+                   +-------------------+
        ↑                                           ↑
        |                                           |
        +------------- API Gateway -----------------+
                      (Single Entry Point)

🧩 Services in This Repository
spring-boot-microservices
├── eureka-server     # Service Discovery Server
├── api-gateway       # API Gateway (Single Entry Point)
├── user-service      # User Management Microservice
├── order-service     # Order Management Microservice
└── README.md

🔹 Eureka Server
Responsibilities

Acts as a Service Discovery Server

All microservices register themselves dynamically

Eliminates hardcoded service URLs

Port
8761

Dashboard
http://localhost:8761

🔹 API Gateway
Responsibilities

Single entry point for all external requests

Routes requests to microservices using service names

Integrates with Eureka for dynamic routing

Handles cross-cutting concerns centrally

Features Implemented

Path-based routing

Load-balanced routing via Eureka

Centralized request logging

Gateway filters

Example Routes
/api/users/**   → user-service
/api/orders/**  → order-service

Port
8080

🔹 User Service
Responsibilities

Manages user data and operations

Exposes REST APIs for user management

Owns its own database (user_db)

Registers with Eureka

Port
8081

🔹 Order Service
Responsibilities

Manages order data and operations

Communicates with User Service to validate users

Owns its own database (order_db)

Registers with Eureka

Port
8082

🛠 Tech Stack

Java 17

Spring Boot

Spring Data JPA

Spring Cloud Netflix Eureka

Spring Cloud Gateway

MySQL

Maven

Docker (planned)

🎯 Key Microservices Concepts Implemented

Monolith vs Microservices design

Database per service

Service discovery using Eureka

API Gateway pattern

REST-based inter-service communication

Client-to-service decoupling

Independent deployment of services

Loose coupling and high cohesion

Stateless REST APIs

Global exception handling

Structured logging (SLF4J)

▶️ How to Run the Project (Local)
1️⃣ Start MySQL

Ensure MySQL is running on port 3306.

2️⃣ Start Eureka Server
cd eureka-server
mvn spring-boot:run

3️⃣ Start API Gateway
cd api-gateway
mvn spring-boot:run

4️⃣ Start User Service
cd user-service
mvn spring-boot:run

5️⃣ Start Order Service
cd order-service
mvn spring-boot:run

🧪 Sample API Flow
POST /api/users    → API Gateway → User Service
POST /api/orders   → API Gateway → Order Service

Flow Explanation

Client sends request to API Gateway

Gateway resolves service via Eureka

Request is routed to target microservice

Services communicate internally if required

Data is stored in respective databases

✔ Clients never access microservices directly
✔ Internal service URLs are hidden

🔮 Planned Enhancements

JWT-based authentication & authorization at API Gateway

Role-based access control

Rate limiting & throttling

Centralized configuration using Config Server

Fault tolerance using Resilience4j

Distributed tracing & monitoring

Docker & Docker Compose

Kubernetes deployment (optional)

👨‍💻 Author

Rajaguru R
Backend Developer | Java | Spring Boot | Microservices

⭐ Why This Project?

This project is built to:

Gain deep, practical understanding of microservices architecture

Implement real-world backend design patterns

Demonstrate production-style Spring Boot microservices

Serve as a strong portfolio project for backend and Java developer roles

✅ Final Notes

This repository evolves continuously as new microservices patterns are implemented.
Each enhancement reflects real-world backend engineering practices, not just theory.

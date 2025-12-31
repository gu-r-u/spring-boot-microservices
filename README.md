🚀 Spring Boot Microservices Architecture

This repository demonstrates a real-world Microservices architecture built using Spring Boot and Spring Cloud.
The project focuses on service decomposition, independent databases, service discovery, inter-service communication, and scalable architecture following industry best practices.

This project is designed as a learning + portfolio project, evolving step by step from core microservices concepts to production-ready patterns.

🧠 Architecture Overview

The system follows a microservices-based architecture where each service:

Is an independent Spring Boot application

Owns its own database

Communicates with other services using REST

Registers itself with a Service Discovery Server

Current and planned architecture:

                +-------------------+
                |   Eureka Server   |
                | (Service Registry)|
                +---------+---------+
                          |
        -------------------------------------------
        |                                         |
+-------------------+                 +-------------------+
|   User Service    |                 |  Order Service   |
|  (MySQL DB)       | <---- REST ---- |  (MySQL DB)      |
+-------------------+                 +-------------------+

        (Next Phase)
                |
        +-------------------+
        |   API Gateway     |
        |  (Single Entry)   |
        +-------------------+

🧩 Services in This Repository
spring-boot-microservices
 ├── eureka-server        # Service Discovery
 ├── user-service         # User Management Microservice
 ├── order-service        # Order Management Microservice
 ├── api-gateway          # API Gateway (upcoming)
 └── README.md

📦 Services Description
🔹 Eureka Server

Acts as Service Discovery Server

All microservices register themselves here

Eliminates hardcoded service URLs

Port: 8761

🔹 User Service

Manages user data

Exposes REST APIs for user operations

Owns its own MySQL database (user_db)

Registered with Eureka

Port: 8081

🔹 Order Service

Manages order data

Communicates with User Service to validate users

Owns its own MySQL database (order_db)

Registered with Eureka

Port: 8082

🔹 API Gateway (Upcoming)

Single entry point for all external requests

Routes requests to appropriate microservices

Will handle authentication, authorization, and filtering

🛠 Tech Stack

Java

Spring Boot

Spring Data JPA

Spring Cloud Netflix Eureka

Spring Cloud Gateway (upcoming)

MySQL

REST APIs

Maven

Docker (upcoming)

🎯 Key Microservices Concepts Implemented

Monolith vs Microservices design

Database per service

Service-to-service communication

Service discovery using Eureka

Independent deployment of services

Loose coupling and high cohesion

Stateless REST APIs

▶️ How to Run the Project (Local)
1️⃣ Start MySQL

Ensure MySQL is running on port 3306.

2️⃣ Start Eureka Server
cd eureka-server
mvn spring-boot:run


Access dashboard:
👉 http://localhost:8761

3️⃣ Start User Service
cd user-service
mvn spring-boot:run

4️⃣ Start Order Service
cd order-service
mvn spring-boot:run

🧪 Sample API Flow

Create a User → POST /users

Create an Order → POST /orders

Order Service validates User via User Service

Data stored in respective databases

🔮 Upcoming Enhancements

API Gateway using Spring Cloud Gateway

JWT-based authentication & authorization

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

Understand microservices deeply, not just theory

Follow industry-standard architecture

Serve as a portfolio project for backend roles

# Java Redis Session Login Demo (Application)
### Companion to: *Java Redis Login Demo – AWS Infrastructure (Terraform)*

This repository contains the **Java Spring Boot application** used by the Terraform-managed AWS infrastructure.  
The app demonstrates **login authentication with Spring Security** and **session sharing using Redis**, allowing multiple EC2 instances behind an AWS ALB to maintain a consistent session.

---

## Overview

This Spring Boot application showcases:

- Redis-backed HTTP Session (`Spring Session + Redis`)
- Multi-instance session sharing across EC2 behind an ALB
- PostgreSQL-backed user authentication
- Redis TTL inspection to visualize session persistence
- Simple UI to demonstrate which EC2 instance serves each request

Architecture:

```
Client → ALB → EC2 Instances → Redis (session store)
                             → RDS (database)
```

---

## Application Architecture

- Spring MVC (controllers & routing)
- Spring Security (authentication)
- Spring Data JPA (database access)
- Spring Session + Redis (session store)
- Thymeleaf (UI templates)
- Actuator (/actuator/health for ALB health checks)

---

## Folder Structure

```
src/
├── main/
│   ├── java/com/example/demo
│   │   ├── config/
│   │   ├── model/
│   │   ├── repo/
│   │   ├── service/
│   │   ├── setup/
│   │   └── web/
│   └── resources/
│       ├── templates/
│       └── application.yaml
└── pom.xml
```

---

## How Sessions Work

Spring Security handles authentication, then Spring Session stores the session **in Redis** instead of local EC2 memory.

Redis session key pattern:

```
spring:session:sessions:<sessionId>
```

Home page displays:
- Session ID  
- Redis key  
- TTL  
- Creation time  
- Last accessed time  
- EC2 hostname  

---

## Endpoints

### `/login`
Spring Security login form.

### `/`
Session dashboard.

### `/actuator/health`
Used by ALB for instance health checks.

---

## Running Locally (Aligned With AWS Deployment Flow)

This application is designed to run on AWS EC2 using files pulled from S3.  
To test or build locally, follow the same flow Terraform expects.

---

### 1. Build the JAR file

Generate the production-ready JAR:

```
mvn clean package
```

The output will be:

```
target/redis-session-login-0.0.1-SNAPSHOT.jar
```

---

### 2. Upload the JAR to your S3 bucket

- Upload the JAR to the same bucket used in Terraform
- Rename it to match what Terraform EC2 user-data expects (redis-session-login.jar):

```
aws s3 cp target/redis-session-login-0.0.1-SNAPSHOT.jar s3://<your-bucket-name>/redis-session-login.jar
```

EC2 user-data will download it automatically on startup:

```
aws s3 cp s3://<bucket>/redis-session-login.jar /home/ubuntu/app.jar
```

---

### 3. Running the App Locally (Optional)

To run the application directly on your local machine:

```
mvn spring-boot:run
```

Optional environment variables:

```
export DB_HOST=localhost
export DB_USER=appuser
export DB_PASS=password
export DB_NAME=appdb
export REDIS_HOST=localhost
```

Redis/PostgreSQL are only required locally if you want to test full authentication and session storage.  
On AWS, these are provided by RDS and ElastiCache.

---

### Demo Login (Local or AWS)

```
username: demo
password: password
```

DataLoader will automatically create this user if it doesn’t exist.

---

## AWS Deployment

Terraform EC2 instances will:

- Install Java + AWS CLI
- Download the JAR from S3
- Inject environment variables
- Start the Spring Boot application

No manual action required after uploading the JAR.

---

## Troubleshooting

- Session lost → Redis not reachable  
- ALB unhealthy → disable DB/Redis health checks  
- Login loop → incorrect Redis hostname or session not stored in Redis  

---

## Summary

This application forms the application layer for the Terraform-managed architecture.  
It demonstrates how session sharing works across multiple EC2 instances via Redis, enabling stateless load balancing behind an AWS ALB.


# SkillSphere Reminder Service

## Overview

SkillSphere Reminder Service is a microservice responsible for sending automated reminders to users who have not logged practice recently. It integrates with the Auth and Skill services, supports scheduled jobs, and can send notifications via email (and optionally Slack or webhooks). The service is stateless, cloud-native, and ready for production deployment.

## Tech Stack

- Java 21, Spring Boot 3
- Spring Scheduling (background jobs)
- Spring Mail (email notifications)
- Spring Web & OpenFeign (REST clients)
- Spring Boot Actuator (health checks)
- Docker/Kubernetes/CI-ready

## Configuration

All configuration is environment-based. See `src/main/resources/application.properties` for details.

## Running the Service

### Local

```sh
./gradlew bootRun
```

### Docker

```sh
docker build -t skillsphere-reminder-service .
docker run --env-file .env -p 8080:8080 skillsphere-reminder-service
```

## Actuator Endpoints

- `/actuator/health` — Health check
- `/actuator/info` — App info

## Integration

- Feign clients are used to call Skill and Auth services for user and email info.
- Configure `AUTH_SERVICE_URL` and `SKILL_SERVICE_URL` in your environment.

---

Further documentation will be added as the service is developed.

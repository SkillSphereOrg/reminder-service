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

## Environment Variables

See `.env.example` for all required variables. Key variables:

- `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD`, `SPRING_MAIL_FROM` — Mail config
- `AUTH_SERVICE_URL`, `SKILL_SERVICE_URL` — Integration URLs
- `SERVER_PORT` — Port to run the service (default: 8080)
- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE` — Actuator endpoints to expose

## Kubernetes Deployment

- Kubernetes manifests are provided in the `k8s/` directory:
  - `reminder-service-deployment.yaml` — Deployment with health checks and env var support (port 8082)
  - `reminder-service-service.yaml` — ClusterIP Service for internal networking (port 8082)
- Use ConfigMaps and Secrets for environment variables and secrets.
- Example:
  ```sh
  kubectl apply -f k8s/reminder-service-deployment.yaml
  kubectl apply -f k8s/reminder-service-service.yaml
  ```
- Integrate with Ingress or API Gateway for external access.

## Running the Service

### Local

```sh
./gradlew bootRun
```

### Docker

```sh
docker build -t skillsphere-reminder-service .
docker run --env-file .env -p 8082:8082 skillsphere-reminder-service
```

## Actuator Endpoints

- `/actuator/health` — Health check
- `/actuator/info` — App info

## Integration

- Feign clients are used to call Skill and Auth services for user and email info.
- Configure `AUTH_SERVICE_URL` and `SKILL_SERVICE_URL` in your environment.

---

Further documentation will be added as the service is developed.

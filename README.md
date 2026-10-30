# Calculator REST API

A RESTful Calculator API built using Java 21, Spring Boot, and Maven, with an end-to-end CI/CD pipeline using Jenkins, Docker, and Docker Compose.

The project demonstrates automated building, testing, containerization, image publishing, and deployment of a Spring Boot application.

## Technologies Used

- Java 21
- Spring Boot and Spring Web
- Spring Boot Actuator
- Maven
- JUnit 5 and MockMvc
- Git and GitHub
- Jenkins
- Docker and Docker Compose
- Nginx

## Features

- Addition, subtraction, multiplication, and division.
- REST endpoints accepting numeric query parameters.
- Structured HTTP 400 responses for invalid or missing parameters and division by zero.
- Spring Boot Actuator health endpoint.
- Automated unit and endpoint testing.
- Multi-stage Docker image with a non-root runtime user.
- Docker Compose deployment with Calculator API and Nginx.
- Jenkins pipeline for automated build, testing, image publishing, deployment, health checks, and email notifications.

## API Endpoints

All calculator operations use HTTP GET requests.

| Operation | Endpoint |
|---|---|
| Addition | `/api/v1/add?a=10&b=5` |
| Subtraction | `/api/v1/subtract?a=10&b=5` |
| Multiplication | `/api/v1/multiply?a=10&b=5` |
| Division | `/api/v1/divide?a=10&b=5` |
| Health Check | `/actuator/health` |

### Example: Addition

Request:

    curl "http://localhost:8080/api/v1/add?a=10&b=5"

Example response:

    {
      "operation": "add",
      "a": 10.0,
      "b": 5.0,
      "result": 15.0
    }

### Example: Division by Zero

Request:

    curl -i "http://localhost:8080/api/v1/divide?a=10&b=0"

Returns HTTP 400:

    {
      "message": "Division by zero is not allowed",
      "error": "Bad Request"
    }

## Project Structure

    calculator-devops/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   └── resources/
    │   └── test/
    │       └── java/
    ├── pom.xml
    ├── mvnw
    ├── Dockerfile
    ├── .dockerignore
    ├── docker-compose.yml
    ├── nginx.conf
    ├── Jenkinsfile
    ├── .gitignore
    └── README.md

## Running Locally

### Prerequisites

- Java 21
- Maven (or the included Maven wrapper)

### 1. Clone the Repository

    git clone https://github.com/bhushitha16/calculator-devops.git
    cd calculator-devops

### 2. Build the Application

    ./mvnw clean package

### 3. Run the Tests

    ./mvnw test

The test suite contains five service-level tests covering the arithmetic operations and division by zero, along with one MockMvc endpoint test.

JUnit XML reports are generated under:

    target/surefire-reports/

### 4. Start the Application

    ./mvnw spring-boot:run

The API is available at:

    http://localhost:8080

## Docker Deployment

The application uses a multi-stage Dockerfile:

- Build stage: Uses Maven and JDK 21 to compile and package the application.
- Runtime stage: Uses a lightweight JRE image and runs the application as a non-root user.

Build the image:

    docker build -t calculator:local .

Run the container:

    docker run -d -p 8080:8080 --name calculator calculator:local

Verify application health:

    curl http://localhost:8080/actuator/health

## Docker Compose Deployment

Docker Compose orchestrates two services:

1. Calculator: Spring Boot REST API.
2. Nginx: Reverse proxy forwarding incoming requests to the Calculator service.

Both services communicate through a shared Docker bridge network.

The Calculator service has an Actuator-based health check. Nginx starts only after the Calculator becomes healthy.

To deploy using the published Docker Hub image:

    export DOCKER_IMAGE=bhushitha16/calculator
    export IMAGE_TAG=14
    docker compose up -d

Replace 14 with the image tag you want to deploy.

Check running services:

    docker compose ps

Access the API through Nginx on port 80:

    curl "http://localhost/api/v1/add?a=10&b=5"

Check application health:

    curl http://localhost/actuator/health

Stop the deployment:

    docker compose down

## CI/CD Pipeline

The project uses a Jenkins Declarative Pipeline defined in the Jenkinsfile.

Jenkins monitors the GitHub repository using Poll SCM and automatically starts a pipeline when changes are detected.

### Pipeline Stages

| Stage | Description |
|---|---|
| Prepare Workspace | Removes stale resources before checkout. |
| Checkout | Retrieves the latest source code from GitHub. |
| Build | Compiles and packages the application using Maven. |
| Test | Runs JUnit tests and publishes test reports. |
| Docker Build | Builds a Docker image tagged with the Jenkins build number. |
| Docker Push | Pushes the image to Docker Hub using Jenkins-managed credentials. |
| Deploy | Deploys the application and Nginx using Docker Compose. |
| Health Check | Verifies application readiness through the Actuator endpoint. |

Jenkins also sends email notifications for successful and failed pipeline executions.

## Docker Hub

Published Docker images:

https://hub.docker.com/r/bhushitha16/calculator

## GitHub Repository

https://github.com/bhushitha16/calculator-devops

## Future Scope

- Extend the pipeline with configuration management using Ansible.
- Deploy the application on Kubernetes.
- Add monitoring and visualization using Prometheus and Grafana.


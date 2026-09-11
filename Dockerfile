# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven files first for dependency caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy application source
COPY src src

# Build the executable JAR
RUN ./mvnw clean package -DskipTests


# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Create a non-root user
RUN useradd --system --create-home --shell /usr/sbin/nologin appuser

# Copy only the executable JAR
COPY --from=build /app/target/calculator-0.0.1-SNAPSHOT.jar app.jar

# Application port
EXPOSE 8080

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]

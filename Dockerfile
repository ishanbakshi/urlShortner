# Multi-stage Dockerfile for Spring Boot Kotlin app
# Build stage
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

# Cache dependencies first
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
COPY gradlew gradlew
RUN chmod +x gradlew

# Pre-download dependencies (empty src to leverage caching)
RUN ./gradlew --no-daemon dependencies || true

# Copy the rest of the source
COPY src src

# Build the application JAR (skip tests for faster image build)
RUN ./gradlew --no-daemon clean bootJar -x test

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Create a non-root user
RUN useradd -ms /bin/bash appuser

# Copy the fat jar from the builder
# The bootJar task produces a single runnable jar in build/libs
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Define a mount point for the H2 file database persistence
VOLUME ["/app/data"]

# Switch to the non-root user
USER appuser

# Optional JVM opts can be passed via JAVA_OPTS env var
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

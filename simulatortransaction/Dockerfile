# =========================
# Build stage
# =========================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Cache: gradle wrapper + build files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew

# Cache dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy source
COPY src ./src

# Build
RUN ./gradlew clean bootJar --no-daemon

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd -m appuser
USER appuser

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8099
ENV SERVER_PORT=8099

ENTRYPOINT ["java","-jar","/app/app.jar"]

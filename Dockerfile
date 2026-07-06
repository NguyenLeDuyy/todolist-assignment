# syntax=docker/dockerfile:1

# ---------- Build stage: có JDK 21 + build ra jar ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy wrapper + file cấu hình build trước (tận dụng cache Docker)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Copy mã nguồn
COPY src src

# Đảm bảo gradlew chạy được rồi build (bỏ -daemon cho môi trường container)
# Lần build đầu sẽ tự tải Gradle 8.14.3 + dependencies (cần internet)
RUN chmod +x gradlew && ./gradlew clean bootJar --no-daemon

# ---------- Runtime stage: chỉ cần JRE 21 để chạy, image nhẹ hơn ----------
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Chỉ copy jar chạy được; glob *-SNAPSHOT.jar KHÔNG khớp ...-SNAPSHOT-plain.jar
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

# App lắng nghe cổng 8080 (theo application.properties)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

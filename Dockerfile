FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package "-DskipTests"

FROM openjdk:17-alpine

RUN apk add --no-cache maven

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/EncryptionAppRemote.jar

CMD ["java", "-jar", "EncryptionAppRemote.jar"]
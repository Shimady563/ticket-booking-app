FROM maven:3.9.7-eclipse-temurin-21-alpine as builder

WORKDIR /

COPY src src
COPY pom.xml .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder target/*.jar app.jar

CMD ["java", "-jar", "/app/app.jar"]

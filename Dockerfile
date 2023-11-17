FROM maven:3.9.5-eclipse-temurin-17-alpine AS build
LABEL authors="Jorge Garcia"

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /app/src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]

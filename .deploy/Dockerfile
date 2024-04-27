# Dockerfile
FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=/build/libs/backend-mainServer-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} energiology.jar
ENTRYPOINT ["java", "-jar", "energiology.jar"]


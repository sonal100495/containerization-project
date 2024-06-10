FROM openjdk:8-jre-alpine
COPY target/my-app-1.0-SNAPSHOT.jar /app/my-app-1.0-SNAPSHOT.jar
CMD ["java", "-jar", "/app/my-app-1.0-SNAPSHOT.jar"]

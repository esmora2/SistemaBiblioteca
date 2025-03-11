# Dockerfile para usuarios-service
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/usuarios-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9091
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/biblioteca
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=admin
ENTRYPOINT ["java", "-jar", "app.jar"]
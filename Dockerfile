FROM openjdk:17-jdk-slim

WORKDIR /app

COPY application-core/src/main/resources/db/changelog /app/db/changelog

COPY application-core/target/mycake.jar /app/mycake.jar

EXPOSE 8080

CMD ["java", "-jar", "application_core.jar"]
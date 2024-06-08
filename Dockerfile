FROM maven:3.8.3-openjdk-17
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .env ./.env
RUN mvn clean install -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/be-1.0-SNAPSHOT.jar"]

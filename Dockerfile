FROM maven:3.8.3-openjdk-17
WORKDIR /app
COPY keystore.jks .
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests
EXPOSE 8443
CMD ["java", "-jar", "target/be-1.0-SNAPSHOT.jar"]
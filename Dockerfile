# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy maven files first
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Fix permissions on mvnw
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:resolve

# Copy source code
COPY src ./src

# Build the app
RUN ./mvnw clean install -DskipTests

# Run the app
EXPOSE 8080
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
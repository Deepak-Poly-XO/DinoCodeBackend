# Use an official Maven image with JDK 17
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# Use an official JDK runtime to run the built JAR
FROM openjdk:17
WORKDIR /app

# Copy built JAR from the builder stage
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Start the application
CMD ["java", "-jar", "app.jar"]

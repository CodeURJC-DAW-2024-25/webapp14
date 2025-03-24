##################################
# Stage 1: Build the application #
##################################

# Base image for building the container
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build

# Sets the working directory for commands to run
WORKDIR /app

# Copies the project's dependencies
COPY pom.xml .

# Downloads required dependencies
RUN mvn clean verify

# Copy project source from local project to compilation container directory
COPY src ./src

# Compiles the project
RUN mvn package -DskipTests

################################
# Stage 2: Run the application #
################################

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre-jammy

# Sets the working directory where the jar file is located
WORKDIR /app

# Copies the jar file into the container
COPY --from=build /app/target/*.jar app.jar

# Exposes the port the app runs on
EXPOSE 8443

# Command to run the app
CMD ["java", "-jar", "app.jar"]

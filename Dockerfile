#In summary, this Dockerfile builds the application, extracts the layers,
#and creates the final Docker image in separate stages.
#This approach can help to reduce the final image size
#and speed up the build process by leveraging Docker's caching mechanism.

# Stage 1: Build the application
FROM gradle:8.7.0-jdk21 AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle clean build --no-daemon

# Stage 2: Extract the layers
FROM openjdk:21-slim AS builder
WORKDIR /app
COPY --from=build /app/build/libs/finbots-0.1.0.jar /app/finbots.jar
RUN java -Djarmode=layertools -jar finbots.jar extract

# Stage 3: Create the Docker final image
FROM openjdk:21-slim
EXPOSE 8080
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
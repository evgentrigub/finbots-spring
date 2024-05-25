# Finbots API

Welcome to the Finbots API!
This application is designed to provide a robust and scalable backend for managing trading bots. 
It's built with Java and Spring Boot, and uses Gradle for dependency management.

## Features

- **User Management**: The application provides comprehensive user management features, including login, signup, profile update, and password change.

- **Bot Management**: Users can create, retrieve, and delete bots. Each bot is associated with a specific user and has its own unique ticker and strategy.

- **Security**: The application uses Spring Security for authentication and authorization. It also includes a JWT utility for generating and validating JWT tokens.

- **Exception Handling**: Custom exceptions are used to handle various error scenarios, providing meaningful error messages to the client.

- **Swagger Integration**: The application includes Swagger for API documentation, making it easy to understand and test the API endpoints.

## Getting Started

To get started with the project, you'll need to have Java and Gradle installed on your machine. Once you have these prerequisites, you can clone the repository and run the application.

Here's how you can run the application:

1. Clone the repository: `git clone https://github.com/evgentrigub/finbots-spring.git`
2. Navigate to the project directory: `cd finbots-spring`
3. Change in application.properties profile to demo `spring.profiles.active=demo`
3. Run the application: `./gradlew bootRun`
4. Access it at `http://localhost:3000`
5. Find the Swagger UI at `http://localhost:3000/swagger-ui.html`

## Contributing

If you find a bug or have a feature request, feel free to open an issue. If you want to contribute code, feel free to open a pull request.

Remember, this is a friendly space. Please follow our code of conduct when interacting with others in this project.

Happy trading!
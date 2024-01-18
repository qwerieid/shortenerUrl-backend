# URL Shortener Application - Backend

This is the backend component of the URL Shortener application, developed using Spring Boot.

## Technologies Used

- **Spring Boot**
- **Spring Data REST**
- **Spring Web**
- **Lombok**
- **Spring Data JPA**: Simplifies data access using JPA.
- **Google Guava**: Provides an implementation of MurmurHash for hashing functions (to hash original URLs into shortened URLs).
- **MapStruct**: Simplifies object mapping.
- **JUnit**: Simplifies testing for Java application.
- **H2 Database**: An in-memory database used for testing.

## Configuration

- Configure MySQL settings in `application.properties` for the dev environment.
- Configure H2 settings in `application-test.properties` for the test environment.

## Building and Running the Application

1. Clone the repository: `git clone <repository-url>`
2. Navigate to the backend directory: `cd backend`
3. Build the project: `./mvnw clean install`
4. Run the application: `./mvnw spring-boot:run`

## Test

* shortener_endpoint.http file provides examples of Java tests to interact with the Shortener API. These tests can be executed to
validate the functionality of the API without the need for external tools like Postman.

  
* Note: The 'test' profile is configured for testing purposes only and does not persist data in the system. The base configuration uses MySQL for persistent data storage.To disable the 'test' profile, simply comment out the line `spring.profiles.active=test` in the `application.properties` file.


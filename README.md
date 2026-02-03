# Digital Wallet Transfer API

A backend application that implements a simplified digital wallet transfer flow, focusing on transactional integrity, clean architecture, and testability.

## Project Context

This project was inspired by a public backend challenge originally proposed by PicPay.
The goal was to implement a **simplified money transfer flow**, following real-world backend constraints such as balance validation, transactional consistency, and integration with external services.

The scope was intentionally kept small to focus on **code quality, architecture decisions, and business rules**, rather than feature quantity.

## Scope

The application focuses exclusively on:

- Money transfers between users
- Business rules enforcement (e.g. merchants cannot initiate transfers)
- Transactional consistency
- External authorization and notification services

User registration, authentication and frontend concerns were intentionally left out to keep the scope focused and maintainable.

## Business Rules Implemented

- Users can transfer money to other users or merchants
- Merchants can only receive transfers
- Balance is validated before each transfer
- Transfers are executed inside a database transaction
- External authorization service is consulted before completion
- In case of any failure, the transaction is rolled back
- Notifications are sent after a successful transfer
- Notification failures do not rollback the transaction

## Architecture

The project follows a layered architecture:

- Controller layer: HTTP handling and request/response mapping
- Service layer: business rules and transaction orchestration
- Repository layer: persistence abstraction using Spring Data JPA
- Domain entities: encapsulate core business logic
- Integration layer: external services (authorizer and notification)

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker & Docker Compose
- JUnit 5
- Mockito

## Testing

The project includes:

- Unit tests for the service layer, covering both success and failure scenarios
- Integration tests for the transfer endpoint, validating the full flow (controller + database)

The tests ensure that business rules are enforced and that the system behaves correctly under different conditions.

## Running the Project

The application uses Docker Compose to provide a PostgreSQL database for local development.

To start the database:

```bash
docker-compose up -d
```

Then run the application:

```bash
mvn spring-boot:run
```

Test with the following flow:

```http request
POST /transfer
Content-Type: application/json

{
  "value": 100.0,
  "payer": 4,
  "payee": 15
}
```

## Possible Improvements

- User registration and authentication (JWT)
- Deposit and withdraw endpoints
- Idempotent transfers
- Asynchronous notifications using a message broker
- Observability (metrics, tracing, logging)
- CI pipeline with automated tests

```

```

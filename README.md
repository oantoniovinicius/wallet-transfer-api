# Digital Wallet Transfer API
🇺🇸 English version

🇧🇷 Versão em português abaixo

## 🇺🇸 English
## Overview

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

## API Usage

After starting the application, you can test the transfer flow using the following endpoint:

```http
POST /transfer
Content-Type: application/json

{
  "value": 100.0,
  "payer": 4,
  "payee": 15
}
```

- payer: ID of the user initiating the transfer (must be a common user)
- payee: ID of the receiving user (common user or merchant)
- value: Amount to be transferred

The endpoint returns 201 Created when the transfer is successful, or an appropriate error status when business rules are violated.

## Possible Improvements

- User registration and authentication (JWT)
- Deposit and withdraw endpoints
- Idempotent transfers
- Asynchronous notifications using a message broker
- Observability (metrics, tracing, logging)
- CI pipeline with automated tests


## 🇧🇷 Português
## Visão Geral

Uma aplicação backend que implementa um fluxo simplificado de transferência entre carteiras digitais, com foco em integridade transacional, arquitetura limpa e testabilidade.

O projeto prioriza a correta aplicação das regras de negócio e a clareza do design, em vez da quantidade de funcionalidades.

## Contexto do Projeto

Este projeto foi inspirado em um desafio público de backend originalmente proposto pelo PicPay.

O objetivo principal foi implementar um fluxo simplificado de transferência de dinheiro, respeitando restrições comuns em sistemas reais, como validação de saldo, consistência transacional e integração com serviços externos.

O escopo foi mantido intencionalmente reduzido para permitir maior foco em qualidade de código, decisões arquiteturais e aplicação das regras de negócio, ao invés de construir uma plataforma completa de pagamentos.

## Escopo

A aplicação foca exclusivamente em:
- Transferências de dinheiro entre usuários
- Aplicação das regras de negócio (ex.: lojistas não podem iniciar transferências)
- Consistência transacional via banco de dados
- Integração com serviços externos de autorização e notificação
- Fluxos de cadastro de usuários, autenticação e frontend foram propositalmente deixados de fora para manter o projeto enxuto, legível e manutenível.

## Regras de Negócio Implementadas

- Usuários podem transferir dinheiro para outros usuários ou lojistas
- Lojistas apenas recebem transferências
- O saldo é validado antes de cada transferência
- As transferências são executadas dentro de uma transação de banco de dados
- Um serviço externo de autorização é consultado antes da finalização
- Em caso de qualquer falha, a transação é revertida
- Notificações são enviadas após uma transferência bem-sucedida
- Falhas no envio de notificação não geram rollback da transação

## Arquitetura

O projeto segue uma arquitetura em camadas, com separação clara de responsabilidades:

- Camada de Controller: tratamento HTTP e mapeamento de requisições/respostas
- Camada de Service: regras de negócio e orquestração transacional
- Camada de Repository: abstração de persistência com Spring Data JPA
- Entidades de domínio: encapsulam a lógica central do negócio
- Camada de Integração: comunicação com serviços externos (autorização e notificação)
  
Essa estrutura foi escolhida para melhorar a testabilidade, legibilidade e manutenibilidade a longo prazo.

## Tecnologias Utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker & Docker Compose
- JUnit 5
- Mockito

## Estratégia de Testes

O projeto inclui:

- Testes unitários na camada de serviço, cobrindo cenários de sucesso e falha
- Testes de integração do endpoint de transferência, validando o fluxo completo (controller + banco de dados)

Os testes garantem que as regras de negócio sejam corretamente aplicadas e que o sistema se comporte conforme esperado.

Executando o Projeto

A aplicação utiliza Docker Compose para fornecer um banco PostgreSQL para desenvolvimento local, garantindo um ambiente consistente e reprodutível.

Suba o banco de dados:
```bash
docker-compose up -d
```
Execute a aplicação: 
```bash
mvn spring-boot:run
```

## Uso da API
Após iniciar a aplicação, o fluxo de transferência pode ser testado através do endpoint abaixo:

```http
POST /transfer
Content-Type: application/json

{
  "value": 100.0,
  "payer": 4,
  "payee": 15
}
```
- payer: ID do usuário que inicia a transferência (deve ser um usuário comum)
- payee: ID do usuário que recebe a transferência (usuário comum ou lojista)
- value: Valor a ser transferido

O endpoint retorna 201 Created quando a transferência ocorre com sucesso, ou um código de erro apropriado quando alguma regra de negócio é violada.

## Possíveis Evoluções
- Cadastro de usuários e autenticação (JWT)
- Endpoints de depósito e saque
- Transferências idempotentes
- Notificações assíncronas com uso de mensageria
- Observabilidade (métricas, tracing e logs estruturados)
- Pipeline de CI com testes automatizados e análise estática

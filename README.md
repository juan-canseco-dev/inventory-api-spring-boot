# Inventory API

`Inventory API` is a Spring Boot 3 backend for managing core inventory operations: catalog data, suppliers, customers, purchases, orders, users, roles, and dashboard reporting.

The project uses a package-by-feature structure, JWT authentication, permission-based authorization, MySQL persistence, and a substantial automated test suite built with Spring Boot Test and Testcontainers.

## Highlights

- JWT-based authentication for API access
- Fine-grained permission checks with Spring Security method authorization
- CRUD flows for users, roles, units, categories, products, suppliers, and customers
- Purchase receiving and order delivery workflows
- Dashboard endpoints for inventory value, low-stock products, top customers, top suppliers, and top-selling products
- Web Sockets for dashboard real time updates.
- Seeded demo data for local development outside the `test` profile

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- MapStruct
- Lombok
- JUnit 5
- Testcontainers

## Project Structure

The codebase is organized by feature instead of by technical layer:

```text
src/main/java/com/jcanseco/inventoryapi
|- bootstrap      # async config and development data initialization
|- catalog        # categories, products, units
|- customers      # customer management
|- dashboard      # reporting and summary endpoints
|- identity       # auth, permissions, roles, users, security config
|- inventory      # stock domain and validation
|- orders         # order workflows and delivery
|- purchases      # purchase workflows and receiving
|- shared         # errors, pagination, utilities, shared validation
`- suppliers      # supplier management
```

Most features follow a consistent layout such as `api`, `domain`, `dto`, `mapping`, `persistence`, and `usecases`.

## Prerequisites

- Java 17
- A running MySQL instance for local development
- Docker Desktop if you want to run the full stack with Compose

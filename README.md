# Wallet Service

A secure and scalable microservice for managing digital wallets and transactions.

## Overview

This wallet service allows users to manage their digital money through a RESTful API. It provides essential functionality for financial operations including deposits, withdrawals, and transfers between wallets, with a strong focus on transaction safety and auditability.

## Key Features

- **Wallet Management**
    - Create new wallets for users
    - Retrieve current wallet balance
    - View historical balance at any point in time

- **Transaction Operations**
    - Deposit funds into wallets
    - Withdraw funds from wallets
    - Transfer funds between wallets

- **Security & Audit**
    - Complete transaction history
    - Audit logging for all operations
    - Secure API endpoints

## Technical Stack

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (for development)
- Maven
- OpenAPI/Swagger for documentation

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
```bash
git clone https://github.com/cskies/wallet-service.git
```

2. Navigate to the project directory:
```bash
cd wallet-service
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

### API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## API Endpoints

### Wallet Operations
- `POST /api/v1/wallets` - Create a new wallet
- `GET /api/v1/wallets/{id}/balance` - Get current balance
- `GET /api/v1/wallets/{id}/balance/historical` - Get historical balance

### Transaction Operations
- `POST /api/v1/wallets/{id}/deposit` - Deposit funds
- `POST /api/v1/wallets/{id}/withdraw` - Withdraw funds
- `POST /api/v1/wallets/{id}/transfer` - Transfer funds to another wallet

### Audit Operations
- `GET /api/v1/audit/entity/{entityType}/{entityId}` - Get audit logs for entity
- `GET /api/v1/audit/user/{userId}` - Get audit logs for user
- `GET /api/v1/audit/type/{entityType}` - Get audit logs by type

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/recargapay/wallet/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── exception/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/recargapay/wallet/
```

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is proprietary and confidential.

## Authors

- Your Name (@cskies)
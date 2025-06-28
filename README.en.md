# Waxberry ServiceHub

Waxberry ServiceHub is a modern microservice architecture project built on Spring Boot 3.x and Spring Cloud 2024. The project adopts a modular design and provides a complete microservice solution, including core functionalities such as authentication and authorization, API gateway, file services, and more.

## Features

- 🚀 Based on Spring Boot 3.x and Spring Cloud 2024
- 🔐 Complete Authentication and Authorization System
- 🌐 API Gateway Service
- 📁 Distributed File Service
- 🛠 Unified Service Management
- 📚 Comprehensive Documentation
- 🔄 Database Version Control
- 🧪 Complete Test Coverage

## Technology Stack

- Java 17
- Spring Boot 3.4.6
- Spring Cloud 2024.0.1
- Spring Security
- MySQL 8.0
- MinIO (Object Storage)
- Flyway (Database Version Control)
- Lombok
- Hutool
- SpringDoc (API Documentation)

## Project Structure

```
waxberry-servicehub/
├── auth/           # Authentication and Authorization Service
├── boot/           # Boot Module
├── common/         # Common Module
├── fileserver/     # File Service
├── gateway/        # API Gateway
└── manager/        # Service Management
```

## Quick Start

### Requirements

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- MinIO Server

### Building the Project

```bash
# Clone the repository
git clone https://gitee.com/jiangxue-waxberry/waxberry-servicehub.git

# Navigate to project directory
cd waxberry-servicehub

# Build the project
mvn clean package
```

### Running the Project

The project supports multiple environment configurations (local, dev, test, prod), with dev being the default:

```bash
# Run in development environment
mvn spring-boot:run -Pdev

# Run in production environment
mvn spring-boot:run -Pprod
```

## Configuration

Main configuration files are located in the `src/main/resources` directory of each module:

- `application.yml`: Base configuration
- `application-dev.yml`: Development environment configuration
- `application-prod.yml`: Production environment configuration

## Documentation

- API Documentation: Access `http://localhost:8080/swagger-ui.html` after starting the service

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.


## Acknowledgments

Thanks to all developers who have contributed to this project! 

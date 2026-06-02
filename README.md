# Spring Boot E-commerce REST API

This is a comprehensive REST API for a modern e-commerce platform, built with Spring Boot. It provides a robust backend for managing products, users, shopping carts, orders, and payments.

## ✨ Features

- **User Authentication**: Secure user registration and login with JWT (JSON Web Tokens).
- **Product Catalog**: Full CRUD functionality for products and categories.
- **Shopping Cart**: Persistent shopping carts for authenticated users.
- **Order Management**: Create orders from the cart and track their status.
- **Payment Integration**: Seamless payment processing with Stripe.
- **Role-Based Access Control**: Differentiated access for regular users and administrators.
- **API Documentation**: Interactive API documentation with Swagger UI.

## 🛠️ Technologies Used

- **Framework**: [Spring Boot](https://spring.io/projects/spring-boot)
- **Language**: [Java](https://www.java.com/)
- **Database**: [PostgreSQL](https://www.postgresql.org/)
- **Authentication**: [Spring Security](https://spring.io/projects/spring-security), [JWT](https://jwt.io/)
- **ORM**: [Spring Data JPA (Hibernate)](https://spring.io/projects/spring-data-jpa)
- **API Documentation**: [SpringDoc OpenAPI (Swagger)](https://springdoc.org/)
- **Build Tool**: [Maven](https://maven.apache.org/)
- **Payment Gateway**: [Stripe](https://stripe.com/)

## 🚀 Getting Started

Follow these instructions to get the project up and running on your local machine for development and testing purposes.

### Prerequisites

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)
- A Stripe account and API keys ([Stripe Dashboard](https://dashboard.stripe.com/register))

### Installation & Configuration

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/spring-api-starter.git
    cd spring-api-starter
    ```

2.  **Create a PostgreSQL database:**
    Create a new database in PostgreSQL for the application.

3.  **Configure the application:**
    Rename `application.yaml.example` to `application.yaml` (or create it) in `src/main/resources/` and update the following properties:

    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://your-db-host:5432/your-db-name
        username: your-db-username
        password: your-db-password
      jpa:
        hibernate:
          ddl-auto: validate # Use 'validate' in production
        show-sql: true
    
    jwt:
      secret: "your-very-secret-key" # Use a strong, long, random string
      access-token-expiration: 7200000 # 2 hours
      refresh-token-expiration: 604800000 # 7 days
    
    stripe:
      secret-key: "sk_test_..." # Your Stripe secret key
      webhook-secret: "whsec_..." # Your Stripe webhook secret
    
    app:
      website-url: "http://localhost:3000" # URL of your frontend application
    ```

### Running the Application

1.  **Build the project:**
    ```bash
    mvn clean install
    ```

2.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will start on the port configured in `application.yaml` (default is 8080).

## 📖 API Documentation

Once the application is running, you can access the interactive API documentation (Swagger UI) at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

This interface provides detailed information about all the available endpoints and allows you to test them directly from your browser.

### API Endpoints Overview

- `/auth/**`: Authentication and user session management.
- `/products/**`: Manage products and categories.
- `/users/**`: User profile management.
- `/carts/**`: Shopping cart operations.
- `/order/**`: View order history.
- `/checkout/**`: Handle the payment and order creation process.
- `/admin/**`: Endpoints restricted to administrators.

The relationships between these tables are defined in the JPA entities located in `src/main/java/com/khangpham/store/entities/`.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a pull request.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


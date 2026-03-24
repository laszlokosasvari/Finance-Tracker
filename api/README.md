# Finance Tracker API 💰

A robust Spring Boot REST API designed to manage personal transactions. Built with a focus on clean architecture, automated documentation, and reliable data persistence.

## 🚀 Tech Stack
* **Java 17** & **Spring Boot 3.4.3**
* **Database:** PostgreSQL
* **Migrations:** Flyway
* **Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Testing:** JUnit 5 & Mockito
* **Build Tool:** Gradle

## 🛠 Features
- **Transaction Management**: Full CRUD operations for income and expenses.
- **Advanced Filtering**: Retrieve transactions between specific dates.
- **Pagination & Sorting**: Efficiently handle large datasets using Spring Data Pageable.
- **Automatic Documentation**: Interactive API testing via Swagger UI.
- **Data Integrity**: Schema versioning with Flyway and DTO pattern for safe data exposure.

## 🏁 Getting Started

### Prerequisites
- JDK 17 or higher
- PostgreSQL instance running (default port 5432)

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/yourusername/finance-tracker.git](https://github.com/yourusername/finance-tracker.git)
   cd finance-tracker

2.  Configure your database in `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
    spring.datasource.username=your_user
    spring.datasource.username=your_password
    ```
3.  Run the application:
    ```bash
    ./gradlew bootRun
    ```

## 📖 API Documentation

Once the app is running, you can explore and test the endpoints directly via Swagger UI:
👉 **[http://localhost:8080/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui/index.html)**

### Key Endpoints

| Method | Endpoint                    | Description |
| :--- |:----------------------------| :--- |
| `GET` | `/api/transactions`         | Get all transactions (Paginated) |
| `POST` | `/api/transactions`         | Create a new transaction |
| `GET` | `/api/transactions/between` | Filter by date range |
| `GET` | `/api/transactions/balance` | Get current total balance |
| `DELETE` | `/api/transactions/`        | Remove a transaction |

## 🧪 Testing

The project includes a comprehensive test suite using **Mockito** to verify service logic and DTO mapping.
To run the tests:

```bash
./gradlew test
```

## 📂 Project Structure

- `com.finance.model`: JPA Entities
- `com.finance.dto`: Request/Response Records
- `com.finance.repository`: Spring Data JPA Repositories
- `com.finance.service`: Business logic and mapping
- `com.finance.controller`: REST Controllers with OpenAPI annotations

# Rate Flow

## Introduction

This application provides an API for managing and retrieving currency pairs. It uses MongoDB as the backend database and Spring Boot for the REST API development.

## Use Cases

### Retrieving Currency Pairs

**Endpoint:** `GET /currency-pairs`

**Description:** This endpoint retrieves all available currency pairs from the database.

- **Success Response:**
    - **Code:** `200 OK`
    - **Content:** List of currency pairs
- **Error Response:**
    - **Code:** `404 Not Found`
    - **Content:** `{ error: "Currency pairs not found" }`

### Getting all pairs by base currency code. USD for example

**Endpoint:** `GET /currency-pairs/base/{currency_code}`

**Description:** Retrieve details of a specific currency pair using its unique identifier.

- **Success Response:**
    - **Code:** `200 OK`
    - **Content:** A list of all currencies associated with the base
- **Error Response:**
    - **Code:** `404 Not Found`
    - **Content:** `{ error: "Currency pairs not found" }`

### Getting pair by combination currency codes. For example USD/UAH

**Endpoint:** `GET /currency-pairs/rate?from{currency_code}=&to={currency_code}`

**Description:** Retrieve of a specific currency pair.

- **Success Response:**
    - **Code:** `200 OK`
    - **Content:** A list of all currencies associated with the base
- **Error Response:**
    - **Code:** `404 Not Found`
    - **Content:** `{ error: "Currency pairs not found" }`
- **Error Response:**
  - **Code:** `400 Bad Request`
  - **Content:** `{ error: "Validation failed"}`

    
## Prerequisites

Before running the application, you need to have installed:

- [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (version 11 or later)
- [MongoDB](https://www.mongodb.com/try/download/community)

## Configuration

Before starting the application, ensure MongoDB is running. You can configure the MongoDB connection settings in `src/main/resources/application.properties`:

In my application, I used the Fixer API. If you're also using this provider, simply specify your access key. If you wish to use a different provider and their API differs, change Interface CurrencyPairsFetch and write your own adapter.

```properties
spring.data.mongodb.uri=mongodb://<username>:<password>@localhost:27017/<dbname>

# Clone the repository

# Change apllication.yaml

# Build the project using Maven
mvn clean install

# Run the application
java -jar target/your-application.jar
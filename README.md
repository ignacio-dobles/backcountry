# Backcountry Product API
#### Assessment by Ignacio Dobles

A lightweight, production-quality Spring Boot 3 REST API for managing products in an eCommerce platform.
Implements full CRUD, filtering, pagination, validation, exception handling, Swagger docs, and an in-memory concurrency-safe data store.

## Running the Application
**Prerequisites**
* Java 17+
* Maven 3.8+

**Build**

```mvn clean install```

**Run**

```mvn spring-boot:run```

Server starts at http://localhost:8080

## API Documentation (Swagger UI)

Interactive API docs:

http://localhost:8080/swagger-ui.html

or

http://localhost:8080/swagger-ui/index.html

OpenAPI JSON spec:

http://localhost:8080/v3/api-docs

## Features
**Product CRUD**

* Create product

* Retrieve single product

* List + filtering + sorting + pagination

* Update product

* Delete product

**In-Memory Repository**

* Uses thread-safe ConcurrentHashMap and simple ID-indexed lists.
* No external DB required.

**Validation**

Strict request validation with Hibernate Validator:

Required fields:

* Price must be numeric

* Inventory must be ≥ 0

* Categories must contain at least one item

**Global Exception Handling**

Consistent JSON responses for:

* Validation errors (400)

* Not-found errors (404)

* Internal errors (500)

**Swagger / OpenAPI**

Auto-generated docs + UI using springdoc-openapi-starter-webmvc-ui.

## Endpoints
### Health Check

**GET** /health

Response

```json
{
"status": "OK"
}
```

## Create Product
**POST** /products

Request

```json
{
"name": "Patagonia Nano Puff Jacket",
"description": "Lightweight insulated jacket",
"brand": "Patagonia",
"price": 189.99,
"inventory": 15,
"categories": ["jackets", "insulated"]
}
```

Response (201 Created)
```json
{
"id": "uuid",
"name": "...",
"description": "...",
"brand": "...",
"price": 189.99,
"inventory": 15,
"categories": ["jackets","insulated"],
"createdAt": "...",
"updatedAt": "..."
}
```
## Get Product by ID
**GET** /products/{id}

404 Response
```json
{
"timestamp": "...",
"status": 404,
"error": "Product with id {id} not found",
"details": {}
}
```

## List Products
**GET** /products

Supports:

* brand

* category

* priceMin

* priceMax

* sort (price, name, or date)

* page

* size

Example:

**GET** /products?brand=Patagonia&priceMax=200&sort=price&page=0&size=10```

### Update Product
**PUT** /products/{id}

Request Body: 

```json
{
"name": "Patagonia Nano Puff Jacket",
"description": "Lightweight insulated jacket",
"brand": "Patagonia",
"price": 189.99,
"inventory": 15,
"categories": ["jackets", "insulated"]
}
```

404 Response

```json
{
"timestamp": "...",
"status": 404,
"error": "Product with id {id} not found",
"details": {}
}
```

### Delete Product
**DELETE** /products/{id}

204 No Content on success

404 Not Found on missing ID

## Testing

Run tests with:

mvn test


The project uses:

JUnit 5

Spring Boot Test

Mockito (optional)

## Packaging

Generate the final JAR:

mvn clean package


Run the JAR:

java -jar target/backcountry-1.0.0.jar

## Notes

No database required — uses in-memory store.

No external services or network dependencies.

Designed to reflect clean, maintainable production code.

Fully Docker-compatible (optional enhancement).
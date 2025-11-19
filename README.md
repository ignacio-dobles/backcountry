# Backcountry Product API
#### Assessment by Ignacio Dobles

A lightweight, production-quality Spring Boot 3 REST API for managing products in an eCommerce platform.
Implements full CRUD, filtering, pagination, validation, exception handling, Swagger docs, and an in-memory concurrency-safe data store.

## Running the Application
**Prerequisites**
* Java 17+
* Maven 3.8+

**Build**

```shell
mvn clean install
```

**Run**

```shell
mvn spring-boot:run
```

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

```shell
mvn test
```

The project uses:

* JUnit 5

* Spring Boot Test

* Mockito

## Packaging

Generate the final JAR:

```shell
mvn clean package
```


Run the JAR:

```shell
java -jar target/backcountry-1.0.0.jar
```

## Notes

No database required — uses in-memory store.

No external services or network dependencies.

Designed to reflect clean, maintainable production code.

Fully Docker-compatible (optional enhancement).

## Sample CURL requests
Run this to start the server:
```shell
mvn spring-boot:run
```

### Create Product (POST /products)
```shell
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Patagonia Nano Puff Jacket",
    "description": "Lightweight insulated jacket",
    "brand": "Patagonia",
    "price": 189.99,
    "inventory": 15,
    "categories": ["jackets", "insulated"]
  }'
```

### Get Product by ID (GET /products/{id})
```shell
curl http://localhost:8080/products/REPLACE_WITH_REAL_ID
```

### List All Products (GET /products)
```shell
curl http://localhost:8080/products
```

### Filter by Brand
```shell
curl "http://localhost:8080/products?brand=Patagonia"
```

### Filter by Category
```shell
curl "http://localhost:8080/products?category=jackets"
```

### Filter by Price Range
```shell
curl "http://localhost:8080/products?priceMin=100&priceMax=200"
```

### Sort by Price (Ascending)
```shell
curl "http://localhost:8080/products?sort=price"
```

### Pagination Example
```shell
curl "http://localhost:8080/products?page=0&size=5"
```

### Update Product (PUT /products/{id})
```shell
curl -X PUT http://localhost:8080/products/REPLACE_WITH_REAL_ID \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Patagonia Nano Puff Jacket (Updated)",
    "description": "Updated lightweight insulated jacket",
    "brand": "Patagonia",
    "price": 169.99,
    "inventory": 12,
    "categories": ["jackets", "winter"]
  }'
```

### Delete Product (DELETE /products/{id})
```shell
curl -X DELETE http://localhost:8080/products/REPLACE_WITH_REAL_ID
```

### Health Check (GET /health)
```shell
curl http://localhost:8080/health
```

## Production-Readiness & Real Database Integration

This project provides a fully functional REST API for managing products, currently backed by an in-memory repository for simplicity and exercise purposes.
Below is a brief overview of how this service would be taken to production and integrated with a real database such as MongoDB.

### Making This API Production-Ready

To move from “assessment-ready” to “production-ready,” it would be desirable to address several layers:

***1. Persistence Layer (Replace In-Memory Repo)***

The current implementation uses an in-memory map (ConcurrentHashMap), which is not persisted and is lost on restart.

Production requires:

* A real database (MongoDB, PostgreSQL, MySQL)

* Proper schema design

* Repository implementations using Spring Data

* Indexes on frequently queried fields (brand, category, price, etc.)

***2. Authentication & Authorization***

Add:

* OAuth2 / JWT authentication

* RBAC for user roles (e.g., Admin, ProductManager)

***3. Logging & Monitoring***

For observability:

* Structured logging (logstash or JSON logs)

* Distributed tracing (OpenTelemetry)

* Metrics and health checks via Spring Actuator

Endpoints such as:

* /actuator/health
* /actuator/metrics
* /actuator/prometheus

***4. Error Handling***

Production would expand it to handle:

* Database constraint violations

* Timeout exceptions

* HTTP client/server failures (if calling external APIs)

* Rate limits

***5. Performance & Scalability***

To support real-world loads:

* Add caching (Redis)

* Index fields used for filtering

***6. CI/CD + Environments***

Introduce:

* GitHub Actions / GitLab CI pipelines

* Automated tests + quality gates

* Deployment to environments (dev / staging / prod)

* Docker images and Kubernetes manifests

***7. API Contract Stability***

Use:

* OpenAPI versioning

* Backwards-compatible changes

* Deprecation strategy for breaking changes

## Integrating With MongoDB (Real DB)

To integrate with MongoDB, the following would be needed:

***1. Add Dependencies***

In pom.xml:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```
***2. Replace the In-Memory Repository***

Instead of:

```java
public class InMemoryProductRepository implements ProductRepository
```


For MongoDB the definition would change to:
```java
public interface ProductRepository extends MongoRepository<Product, UUID> {
List<Product> findByBrandIgnoreCase(String brand);
List<Product> findByCategoriesContaining(String category);
}
```

***3. Annotate Product Model for MongoDB***
```java
   @Document(collection = "products")
   public class Product {

   @Id
   private UUID id;

   @Indexed
   private String brand;

   @Indexed
   private List<String> categories;

   // etc.
   }
```

Indexes improve query performance.

***4. Configure MongoDB***

In application.yml:
```yaml
spring:
data:
mongodb:
uri: mongodb://localhost:27017/backcountry
```

For production:

* Use environment variables

* Enable TLS

* Use a replica set (for redundancy and to support read-only operations)

***5. Update Service Logic***

Remove in-memory filtering and sorting and instead rely on the DB:

* MongoDB queries for brand, category, and price range

* Sorting handled by MongoDB’s .sort()

* Pagination handled with Pageable

***6. Add Repository Tests***

Use Testcontainers with MongoDB to ensure reliable integration tests:

```java
@Container
static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");
```

***7. Add Seed Data for Local Dev***

Create a data-mongodb.js seed script or Spring CommandLineRunner to load sample products.
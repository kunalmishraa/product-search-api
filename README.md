# Product Search API

A backend product search and inventory management API built with **Java, Spring Boot, MongoDB, Elasticsearch, Redis, Docker, and Jenkins**.

The primary goal of this project is to understand how different backend technologies work together in a real-world style application.

The project demonstrates:

- REST API development using Spring Boot
- MongoDB for primary data persistence
- Elasticsearch for product search
- Redis for caching
- Redis CLI for cache inspection and management
- Docker and Docker Compose for local infrastructure
- Mongo Express for MongoDB web UI
- Kibana for Elasticsearch web UI
- Jenkins for CI/CD
- Clean layered backend architecture

---

# Features

## Product Management

- Create products
- Retrieve all products
- Retrieve a product by ID
- Retrieve products by category
- Retrieve products by brand
- Retrieve popular products

## Product Search

Elasticsearch is used for product search functionality.

Supported search capabilities include:

- Full-text product search
- Search by product name
- Search by description
- Filter by category
- Filter by brand
- Filter by price range
- Pagination
- Sorting
- Combined search and filtering

## Inventory Management

- Create inventory for a product
- Retrieve inventory for a product
- Retrieve all inventory

## Redis Caching

Redis is used to demonstrate caching.

Potential cache use cases include:

- Product-by-ID caching
- Popular-products caching
- Cache expiration
- Cache hit/miss behavior

Redis CLI can be used to inspect and manage cached data.

## Docker

MongoDB, Redis, Elasticsearch, Mongo Express, and Kibana are containerized using Docker Compose.

## CI/CD

Jenkins is planned for automating:

- Build
- Test
- Docker image creation
- Application deployment

---

# Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend framework |
| Spring Web | REST APIs |
| Spring Data MongoDB | MongoDB integration |
| MongoDB | Primary database |
| Mongo Express | MongoDB web UI |
| Elasticsearch | Product search |
| Kibana | Elasticsearch web UI |
| Redis | Caching |
| Redis CLI | Redis management and debugging |
| Docker | Containerization |
| Docker Compose | Local infrastructure |
| Jenkins | CI/CD |
| Maven | Dependency management and build |
| Git | Version control |
| GitHub | Source code repository |

---

# Architecture

The application follows a layered architecture.

```text
                         Client
                           |
                           v
                  Spring Boot REST API
                           |
                           v
                      Controllers
                           |
                           v
                       Services
                           |
             +-------------+-------------+
             |             |             |
             v             v             v
          MongoDB    Elasticsearch     Redis
             |             |             |
             |             |             |
       Primary Data    Search Index     Cache
```

---

# High-Level Data Flow

## Product Creation

When a product is created:

```text
Client
  |
  | POST /api/products
  v
ProductController
  |
  v
ProductService
  |
  +------> MongoDB
  |
  +------> Elasticsearch
```

MongoDB acts as the primary source of product data.

Elasticsearch maintains a searchable representation of the product.

---

# Product Retrieval

For:

```text
GET /api/products/{productId}
```

Redis can be used as a cache.

```text
Client
  |
  v
ProductController
  |
  v
Redis
  |
  +---- Cache HIT ----> Return Product
  |
  +---- Cache MISS
             |
             v
          MongoDB
             |
             v
           Redis
             |
             v
        Return Product
```

This demonstrates the Cache-Aside pattern.

---

# Product Search

Search requests are handled by Elasticsearch.

Example:

```text
GET /api/products/search?q=iphone
```

Flow:

```text
Client
  |
  v
ProductSearchController
  |
  v
ProductSearchService
  |
  v
Elasticsearch
  |
  v
Search Results
```

Elasticsearch can perform:

- Full-text search
- Multi-field search
- Filtering
- Range queries
- Sorting
- Pagination

---

# Docker Architecture

The local development environment uses Docker Compose.

```text
Docker Compose
│
├── MongoDB
│   └── Port: 27017
│
├── Mongo Express
│   └── Port: 8081
│
├── Redis
│   └── Port: 6379
│
├── Elasticsearch
│   └── Port: 9200
│
└── Kibana
    └── Port: 5601
```

---

# Docker Services

## MongoDB

MongoDB is used as the primary database.

```text
Port: 27017
```

Connection from the host:

```text
mongodb://localhost:27017
```

---

## Mongo Express

Mongo Express provides a browser-based UI for MongoDB.

```text
Port: 8081
```

Open:

```text
http://localhost:8081
```

It can be used to:

- View databases
- View collections
- Inspect documents
- Create collections
- Inspect MongoDB data

---

## Redis

Redis is used for caching.

```text
Port: 6379
```

Redis CLI can be accessed using:

```bash
docker exec -it product-redis redis-cli
```

Test Redis:

```text
PING
```

Expected response:

```text
PONG
```

---

## Elasticsearch

Elasticsearch is used for product search.

```text
Port: 9200
```

Access Elasticsearch:

```text
http://localhost:9200
```

---

## Kibana

Kibana provides a web UI for Elasticsearch.

```text
Port: 5601
```

Open:

```text
http://localhost:5601
```

Kibana can be used to:

- Execute Elasticsearch queries
- Inspect indexes
- Inspect documents
- Test search queries
- Create data views
- Analyze Elasticsearch data

---

# API Endpoints

The application currently follows a simple REST API structure and intentionally uses only `GET` and `POST`.

---

## Product APIs

### Create Product

```http
POST /api/products
```

Creates a new product.

Example request:

```json
{
  "name": "iPhone 17",
  "description": "Apple smartphone",
  "brand": "Apple",
  "category": "Electronics",
  "price": 79999
}
```

---

### Get All Products

```http
GET /api/products
```

Optional parameters:

```text
page
size
sort
```

Example:

```http
GET /api/products?page=0&size=20
```

---

### Get Product By ID

```http
GET /api/products/{productId}
```

Example:

```http
GET /api/products/P1001
```

---

### Get Products By Category

```http
GET /api/products/category/{category}
```

Example:

```http
GET /api/products/category/ELECTRONICS
```

---

### Get Products By Brand

```http
GET /api/products/brand/{brand}
```

Example:

```http
GET /api/products/brand/APPLE
```

---

### Get Popular Products

```http
GET /api/products/popular
```

This endpoint can demonstrate Redis caching.

---

# Search API

## Search Products

```http
GET /api/products/search
```

Supported parameters:

```text
q
category
brand
minPrice
maxPrice
page
size
sort
```

Example:

```http
GET /api/products/search?q=iphone
```

Search with category:

```http
GET /api/products/search?q=iphone&category=ELECTRONICS
```

Search with brand:

```http
GET /api/products/search?q=iphone&brand=APPLE
```

Search by price range:

```http
GET /api/products/search?q=iphone&minPrice=50000&maxPrice=100000
```

Combined search:

```http
GET /api/products/search?q=iphone&category=ELECTRONICS&brand=APPLE&minPrice=50000&maxPrice=100000
```

Pagination:

```http
GET /api/products/search?q=iphone&page=0&size=10
```

Sorting:

```http
GET /api/products/search?q=iphone&sort=price,asc
```

---

# Inventory APIs

## Create Inventory

```http
POST /api/products/{productId}/inventory
```

Example:

```http
POST /api/products/P1001/inventory
```

Example request:

```json
{
  "quantity": 100
}
```

---

## Get Product Inventory

```http
GET /api/products/{productId}/inventory
```

Example:

```http
GET /api/products/P1001/inventory
```

---

## Get All Inventory

```http
GET /api/inventory
```

Optional parameters:

```text
page
size
```

Example:

```http
GET /api/inventory?page=0&size=20
```

---

# Health Check

Spring Boot Actuator provides:

```http
GET /actuator/health
```

This endpoint can be used by Docker, Jenkins, or deployment infrastructure to check application health.

---

# Project Structure

The project follows a layered architecture.

```text
src/
└── main/
    └── java/
        └── com/
            └── example/
                └── productsearch/
                    │
                    ├── controller/
                    │   ├── ProductController.java
                    │   ├── ProductSearchController.java
                    │   └── InventoryController.java
                    │
                    ├── service/
                    │   ├── ProductService.java
                    │   ├── ProductServiceImpl.java
                    │   ├── ProductSearchService.java
                    │   ├── ProductSearchServiceImpl.java
                    │   ├── InventoryService.java
                    │   └── InventoryServiceImpl.java
                    │
                    ├── repository/
                    │   ├── ProductRepository.java
                    │   └── InventoryRepository.java
                    │
                    ├── elasticsearch/
                    │   ├── ProductSearchDocument.java
                    │   └── ProductSearchRepository.java
                    │
                    ├── redis/
                    │   ├── ProductCacheService.java
                    │   └── RedisKeyBuilder.java
                    │
                    ├── model/
                    │   ├── Product.java
                    │   └── Inventory.java
                    │
                    ├── dto/
                    │   ├── ProductRequest.java
                    │   ├── ProductResponse.java
                    │   ├── ProductSearchRequest.java
                    │   ├── InventoryRequest.java
                    │   └── InventoryResponse.java
                    │
                    ├── exception/
                    │
                    └── config/
```

---

# Running the Project Locally

## Prerequisites

Install the following:

- Java 21
- Maven
- Docker Desktop
- Git

Optional:

- IntelliJ IDEA
- Postman
- MongoDB Compass
- VS Code

---

# Start Docker Infrastructure

From the project root:

```bash
docker compose up -d
```

Check running containers:

```bash
docker compose ps
```

Expected services:

```text
product-mongodb
product-mongo-ui
product-redis
product-elasticsearch
product-kibana
```

---

# Stop Docker Infrastructure

```bash
docker compose down
```

To remove containers and persistent volumes:

```bash
docker compose down -v
```

> Warning: `docker compose down -v` removes the MongoDB, Redis, and Elasticsearch data stored in the Docker volumes.

---

# Verify MongoDB

Open Mongo Express:

```text
http://localhost:8081
```

Or connect using:

```text
mongodb://localhost:27017
```

---

# Verify Redis

Run:

```bash
docker exec -it product-redis redis-cli
```

Then:

```text
PING
```

Expected:

```text
PONG
```

---

# Verify Elasticsearch

Open:

```text
http://localhost:9200
```

Or use:

```bash
curl http://localhost:9200
```

---

# Verify Kibana

Open:

```text
http://localhost:5601
```

Use:

```text
Kibana → Dev Tools → Console
```

to execute Elasticsearch queries.

Example:

```http
GET _cat/indices?v
```

---

# Running Spring Boot

Start the Docker infrastructure first:

```bash
docker compose up -d
```

Then run the Spring Boot application using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application will normally be available at:

```text
http://localhost:8080
```

---

# Example Development Workflow

A typical development workflow is:

```text
1. Start Docker
       |
       v
2. MongoDB
   Redis
   Elasticsearch
       |
       v
3. Start Spring Boot
       |
       v
4. Create Product
       |
       v
5. Store Product in MongoDB
       |
       v
6. Index Product in Elasticsearch
       |
       v
7. Request Product
       |
       v
8. Check Redis Cache
       |
       v
9. Search Product
       |
       v
10. Elasticsearch returns results
```

---

# Elasticsearch Development

Kibana can be used to manually test Elasticsearch queries.

Example:

```http
GET products/_search
{
  "query": {
    "match": {
      "name": "iphone"
    }
  }
}
```

Example multi-field search:

```http
GET products/_search
{
  "query": {
    "multi_match": {
      "query": "Apple",
      "fields": [
        "name",
        "description",
        "brand"
      ]
    }
  }
}
```

These queries are useful for understanding Elasticsearch before implementing equivalent queries in Spring Boot.

---

# Redis Development

Redis CLI can be used to inspect application caching.

Connect:

```bash
docker exec -it product-redis redis-cli
```

Useful commands:

```text
PING
SET key value
GET key
DEL key
EXISTS key
EXPIRE key 60
TTL key
KEYS *
SCAN 0
HGET
HSET
LPUSH
LRANGE
```

Example:

```text
SET product:P1001 "cached-product"
```

Retrieve:

```text
GET product:P1001
```

---

# MongoDB Development

Mongo Express can be used to visually inspect MongoDB.

Open:

```text
http://localhost:8081
```

MongoDB will contain the primary product and inventory data.

Example logical structure:

```text
productdb
│
├── products
│
└── inventory
```

---

# Docker Networking

When all services are running inside Docker Compose, services communicate using their Docker Compose service names.

For example:

```text
MongoDB:
mongodb:27017

Redis:
redis:6379

Elasticsearch:
elasticsearch:9200
```

When Spring Boot is running directly on the host machine, use:

```text
localhost:27017
localhost:6379
localhost:9200
```

When Spring Boot is later containerized, use the Docker service names instead.

---

# CI/CD with Jenkins

Jenkins is intended to automate the application build and deployment process.

The planned pipeline is:

```text
GitHub
   |
   v
Jenkins
   |
   +---- Checkout
   |
   +---- Build
   |
   +---- Test
   |
   +---- Package
   |
   +---- Build Docker Image
   |
   +---- Deploy
   |
   v
Application
```

A `Jenkinsfile` will eventually define the pipeline stages.

---

# Future Improvements

This project can be extended with:

- Authentication and authorization
- JWT
- Spring Security
- Product categories
- Product reviews
- Product ratings
- Advanced Elasticsearch queries
- Elasticsearch autocomplete
- Elasticsearch fuzzy search
- Redis distributed caching
- Cache invalidation
- MongoDB indexes
- Elasticsearch mappings
- API validation
- Global exception handling
- Unit tests
- Integration tests
- Testcontainers
- Dockerized Spring Boot application
- Jenkins CI/CD pipeline
- Automated Docker image publishing
- Deployment to a cloud platform
- Monitoring and observability
- Spring Boot Actuator
- Prometheus
- Grafana

---

# Learning Objectives

The main purpose of this project is to gain practical understanding of:

## Spring Boot

- REST API development
- Controllers
- Services
- Dependency Injection
- DTOs
- Exception handling
- Configuration
- Actuator

## MongoDB

- Documents
- Collections
- MongoDB queries
- Indexes
- Spring Data MongoDB
- Repository pattern

## Elasticsearch

- Indexes
- Documents
- Mappings
- Full-text search
- Match queries
- Term queries
- Boolean queries
- Range queries
- Filters
- Sorting
- Pagination
- Search optimization

## Redis

- Key-value storage
- Caching
- TTL
- Cache hit/miss
- Redis data structures
- Redis CLI
- Cache invalidation

## Docker

- Images
- Containers
- Volumes
- Networks
- Docker Compose
- Container-to-container communication

## Jenkins

- CI/CD
- Automated builds
- Testing
- Docker image creation
- Deployment pipelines

---

# Project Status

🚧 **Currently under development**

The project is being developed incrementally to understand each technology and how they work together in a production-style backend architecture.

---

# Author

**Kunal Mishra**

GitHub:

https://github.com/kunalmishraa

Repository:

https://github.com/kunalmishraa/product-search-api

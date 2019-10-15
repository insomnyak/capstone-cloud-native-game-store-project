# Cloud Native Game Store Project
<span style="color:lime;">**Contributors:** insomnyak | trishdsouza</span>

This Game Store API project is a set of REST APIs that takes a basic unfinished monolith style application and transforms it into a suite of seven microservices following cloud native architectural design standards. 


**<i>SECTIONS</i>**
- [Structure & Methodology](#structure-methodology)
- [Cloud-Native Design Elements](#design-elements)
- [Architerutal Specifications](#architectural-specs)
- [Microservices Baseline UML Diagram](#baseline-uml)
- Swagger OpenAPI Documentation
    - [Retail API](Retail-API-Documentation-OpenApi-1.0.01.yaml)
    - [Admin API](admin-api.yaml)
- [Cache Configuration](#cache-config)

**<h2 id="structure-methodology">Structure & Methodology</h2>**
The implemented solution adheres to the following:
- Each service is in a separate IntelliJ IDEA project using Maven.
- The project is built using Spring Boot and Spring MVC
- Project was managed using Pivotal Tracker
- The project completetion was divided into iterations, each of which contained multiple stories
- Used Test-Driven Development (TDD) 

**<h2 id="design-elements">Cloud-Native Design Elements</h2>**
- Configuration Server
- Service Registry (Eureka)
- AMQP Queues (using RabbitMQ)
- Circuit Breaker (using Netflix Hystrix & HystrixFeign)
- Caching
- Microservices
- Backend for Frontend (BFF) edge services

**<h2 id="architectural-specs">Architerutal Specifications</h2>**
- All services use the Configuration Server for the config files
- All backing services register with the Service Registry. The Retail API and Admin API web services use the Service Registry to locate the backing services.
- Communication with the backing services uses Feign Clients in a Service Layer Component
- All DAOs use JdbcTemplates and Prepared Statements
- Caching is incorporated as appropriate in each service: see [Cache Configuration](#cache-config) for further details
- Error Handling follows aspect oriented programming principles. It implements ControllerAdvice to handle exceptions, returning the appropriate HTTP Status Code, which includes handling all violations of business rules.
- JSR 303 Validation is used where appropriate
- Jenkins CI/CD: each service has its own Jenkins pipeline with three stages: 1) Build, 2) Test, 3) Deliver
- REST APIs are documented using Swagger: see [retail api documentation](Retail-API-Documentation-OpenApi-1.0.01.yaml), and [admin api documentation](admin-api.yaml)
- Retail API to Level Up communication: 
    - Level Up point updates are submitted via a queue
    - Level Up Service inquires are run through a circuit breaker
- Admin API Security Rules:
    - All Admin API endpoints require authentication
    - Authorization:
        - Admin Role - can access all endpoints
        - Manager Role - can create, read, and update all items in the system
        - Team Lead Role - can read and update all items in the system. Can create customers.
        - Employee Role - can read all items and update inventory in the system.
    - Uses default Spring Security schema
    - Passwords are hashed with BCrypt


**<h2 id="baseline-uml">Microservices Baseline UML Diagram</h2>**
![uml diagram](/_images/cloud-native-design-uml.png)

**<i>services</i>**
- **<span style="color:lightblue">Retail API Service:</span>** The Retail API is a BFF edge service that contains all API endpoints for searching inventory and purchasing products. This API does not require authentication. It is [documented](Retail-API-Documentation-OpenApi-1.0.01.yaml) using Swagger OpenAPI.
    - *<span style="color:orange">Custom Configurations:</span>*
        - HystrixFeign Client Configuration
        - Feign Client Configuration: including Decoder, ErrorDecoder
    - *<span style="color:orange">RabbitMQ:</span>*
        - RabbitTemplate used for update requests. These are sent via a new Thread so the application process is no paused
        - AsyncRabbitTemplate used for save and consolidation requests
- **<span style="color:lightblue">Admin API Service:</span>** The Admin API is a BFF edge service that contains all the endpoints for CRUD operations of the following (see [documentation](admin-api.yaml)):
    - Customers
    - Products
    - Inventory
    - Orders
    - Level Up Points
- **<span style="color:lightblue">Level Up Service</span>**:
    - *<span style="color:orange">Eventual Consistency Design:</span>* there are quite a few failure points at which the total points may become out-of-sync with the actual points that a customer should have earned for a submitted order. Eventual consistency is achieved when a customer retrieves their customer summary, a customer view model object that contains a list of of invoices. When this summary is stitched together from the backend services a check is performed on the awarded points per invoice. If the awarded points total is greater than the retrieved total points from the Level Up Service, an update request is triggered to update the tuple.
    - *<span style="color:orange">Queue Exchange Type:</span>* Topic Exchange
- **<span style="color:lightblue">Invoice Service</span>**  
- **<span style="color:lightblue">Customer Service</span>** 
- **<span style="color:lightblue">Product Service</span>** 
- **<span style="color:lightblue">Inventory Service</span>** 

**<h2 id="business-logic">Business Logic</h2>**
- Retail API
    - 10 Level Up points are awarded for each $50 purchased.
    - These points are not pro-rated.
        - For example:
            - A $49 order gets zero Level Up points.
            - A $99 order gets 10 Level Up points.
            - A $110 order gets 20 Level Up points.
    - Level Up points are submitted when the order is submitted.
    - Level Up points totals are returned as part of the completed invoice.
    - Order quantity must be greater than zero and less than or equal to the number of items in inventory.
    - Orders must contain valid products.
    - An order must contain a valid customer.
- Admin API security rules
    - All Admin API endpoints require authentication.
    - Admin Role
        - Can access all endpoints.
    - Manager Role
        - Can Create, Read, and Update all items in the system.
    - Team Lead Role
        - Can Read and Update all items in the system.
        - Can Create Customers in the system.
    - Employee Role
        - Can read all items in the system.
        - Can Update Inventory in the system.

**<h2 id="cache-config">Cache Configuration</h2>**
- Retail API Controller:
    - **`@Cacheable` paths:**  
        - GET /customer/{customerId}
        - GET /customer/{customerId}/invoices
    - **`@CachePut` path:**
        - POST /order > evicts cache labeled with *customerId* found in order.
    - **`@CacheEvict` path:**
        - DELETE /customer/{customerId}/cache > provides end-user or client with the ability to delete a cached item as needed.
- Invoice Service Controller:
    - **`@CachePut(key = "#result.getInvoiceId()")`**
        - POST /invoice
    - **`@Cacheable(key = "'ivmId:' + #invoiceId", condition = "#result != null")`**
        - GET /invoice/{invoiceId}
    - **CacheEvict**
        - DELETE /invoice/{invoiceId}
            ```java
            @Caching(evict = {
                    @CacheEvict(key = "'ivmId:' + #invoiceId"),
                    @CacheEvict(key = "'countByInvoiceId: ' + #invoiceId")
            })
            ```
    - **Caching**
        - PUT /invoice
            ```java
            @Caching(evict = {
                    @CacheEvict(key = "'ivmId:' + #ivm.getInvoiceId()"),
                    @CacheEvict(key = "'countByInvoiceId: ' + #ivm.getInvoiceId()")
            })
            ```
- Level Up Controller
    - **`@CachePut(key = "'id:' + #result.getLevelUpId()")`**
        - POST /levelUp
    - **`@Cacheable(key = "'id:' + #levelUpId")`**
        - GET /levelUp/{levelUpId}
    - **Caching**
        - PUT /levelUp
            ```java
            @Caching(evict = {
                    @CacheEvict(key = "'id:' + #levelUp.getLevelUpId()"),
                    @CacheEvict(key = "'countById:' + #levelUp.getLevelUpId()")
            })
            ```
        - DELETE /levelUp/{levelUpId}
            ```java
            @Caching(evict = {
                    @CacheEvict(key = "'id:' + #levelUpId"),
                    @CacheEvict(key = "'countById:' + #levelUpId")
            })
            ```
    - **Cacheable**
        - GET /levelUp/count/{levelUpId}
            ```java
            @Cacheable(key = "'countById:' + #levelUpId")
            ```
        - GET /levelUp/customer/{customerId}/memberDate
            ```java
            @Cacheable(key = "'memberDate:' + #customerId")
            ```
    - **`@CacheEvict(allEntries = true)`**
        - DELETE /levelUp/customer/{customerId}
        - PUT /levelUp/customer/{customerId}/consolidate
- Product Controller
    - **`@CachePut(key = "#result.getProductId()")`**
        - POST /product
    - **`@Cacheable`**
        - GET /product/{productId}
    - **`@CacheEvict(key = "#product.getProductId()")`**
        - PUT /product
    - **`@CacheEvict`**
        - DELETE /product/{productId}
- Inventory Controller
    - **`@CachePut(key = "'id:' + #result.getInventoryId()")`**
        - POST /inventory
    - **`@CacheEvict(key = "'id:' + #inventory.getInventoryId()")`**
        - PUT /inventory
    - **Caching**
        - DELETE /inventory/{inventoryId}
            ```java
            @Caching(evict = {
                    @CacheEvict(key = "'id:' + #inventoryId"),
                    @CacheEvict(key = "'count:' + #inventoryId")
            })
            ```
    - **`@Cacheable`**
        - GET /inventory/{inventoryId}
    - **`@Cacheable(key = "'count:' + #inventoryId")`**
        - GET /inventory/{inventoryId}/count
- Customer Controller
    - **`@CachePut(key = "#result.getCustomerId()")`**
        - POST /customer
    - **`@Cacheable`**
        - GET /customer/{customerId}
    - **`@CacheEvict(key = "#customer.getCustomerId()")`**
        - PUT /customer
    - **`@CacheEvict`**
        - DELETE /customer/{customerId}
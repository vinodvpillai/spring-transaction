# Spring Boot Transaction Service
This project provide overview about the spring transaction management using the annotation @Transactional.
##
### Prerequisites
- JDK 1.8
- Maven
- PostgreSQL

## Quick Start

### Clone source
```
git clone https://github.com/vinodvpillai/spring-transaction.git
cd spring-transaction
```

```
PostgreSQL START
```

### Build
```
mvn clean package
```

### Run
```
java -jar spring-transaction.jar
```

### Endpoint details:

##### 1. CustomerController - Add new customer (CURL Request):

```
curl -X POST \
  http://localhost:8082/customers \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: ca959693-072f-4f32-9e6f-239e2819b257' \
  -H 'cache-control: no-cache' \
  -d '{
	"name": "Vinod",
	"emailId": "vinod@yopmail.com",
	"address1": "Gandhinagar",
	"address2":	"Gujarat"
}'
```
##### 2. CustomerController - Get customer (CURL Request):

```
curl -X GET \
  http://localhost:8082/customers/vinod@yopmail.com \
  -H 'Postman-Token: e60c4a3f-1663-4d32-9170-2e31f35faf66' \
  -H 'cache-control: no-cache'
```

##### 3. CustomerController - Update customer (CURL Request):
```
curl -X PUT \
  http://localhost:8082/customers/vinod@yopmail.com \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: ea4f47ac-5deb-40b2-b47f-c1c546ef8508' \
  -H 'cache-control: no-cache' \
  -d '{
	"name": "Vinod",
	"address1": "Ahmedabad",
	"address2":	"Gujarat"
}'
```

##### 4. CustomerController - Delete customer (CURL Request):
```
curl -X DELETE \
  http://localhost:8082/customers/vinod@yopmail.com \
  -H 'Postman-Token: 33db3ab6-db4d-4a33-b90c-fb47411da16a' \
  -H 'cache-control: no-cache'
```


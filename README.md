# QA Automation – API Testing Project

## Overview
This project is a QA automation training project focused on API testing.
It demonstrates how to design, implement and execute automated tests using Java and common QA tools.

The goal is to showcase:
- API test automation best practices
- Clean test structure
- Data-driven testing
- CI execution with Jenkins

---

## Tested API
Public REST API used for testing:
https://jsonplaceholder.typicode.com

Example endpoints:
- GET /posts
- GET /posts/{id}
- POST /posts

---

## Tech Stack
- Java
- Maven
- JUnit 5
- RestAssured
- Jackson (JSON parsing)
- Jenkins (CI)

---

## Project Structure
```text
.
├── Jenkinsfile
├── docker-compose.yml
├── db.json
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── model
    │           └── Post.java
    └── test
        ├── java
        │   ├── api
        │   │   └── PostApiDockerTest.java
        │   ├── config
        │   │   └── ApiConfig.java
        │   └── utils
        │       └── TestDataUtils.java
        └── resources
            ├── logback-test.xml
            └── posts.json
```


---

## Test Coverage
The project includes:
- API availability tests (status codes)
- Response body validation
- Path parameter validation
- Data-driven tests using JSON files
- Positive and negative test cases

---

## CI Execution
Tests can be executed automatically using Jenkins.

The Jenkins pipeline performs:
- Source code checkout
- Maven build and test execution
- Automated test result reporting

## How to Run Tests
Run all tests locally using Maven:
```bash
mvn clean test
```

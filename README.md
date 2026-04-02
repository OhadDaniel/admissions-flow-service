# Admissions Flow Service

## Overview

This project implements an admissions flow system that simulates a multi-step onboarding process for users.

The system allows:
- Creating users
- Progressing through a predefined flow of steps
- Executing tasks within each step
- Tracking user status (IN_PROGRESS / ACCEPTED / REJECTED)

---
## Project Structure

The project is organized into clear layers, each with a single responsibility:

- `controller/` – REST API endpoints (user actions and task execution)
- `facade/` – orchestrates the admissions flow and enforces business rules
- `flow/` – defines the structure and order of steps in the process
- `task/` – individual task implementations (business logic)
- `repository/` – in-memory storage of user progress
- `dto/` – request and response objects
- `validation/` – shared validation utilities
- `builder/` – system initialization (wiring flow and tasks together)
- `config/AppConfig` – central configuration class responsible for wiring core components (used in application setup and tests)

---

## Design Overview

For a deeper understanding of the system architecture, design decisions, and extensibility:

👉 See the full design document:  
`docs/design.md`

This document explains:
- System architecture and flow
- Design patterns used
- How to extend the system (add tasks / steps)
- Tradeoffs and design decisions

Reading it is recommended to fully understand the system design.

---

## Prerequisites

Make sure the following tools are installed:

- Java 17
- Maven (3.8+)

Verify installation:

```bash
java -version
mvn -version
```

---

## Setup

1. Clone the repository:

```bash
git clone <your-repo-url>
cd admissions-service
```

2. Build the project:

```bash
mvn clean install
```

---

## Running the Application

Start the server:

```bash
mvn spring-boot:run
```

The application will run on:

```
http://localhost:8080
```

---

## API Endpoints

Base URL:

```
/admissions/users
```

---

### Create User

**POST** `/admissions/users?email=test@mail.com`

Returns a unique user ID.

---

### Get Full Flow

**GET** `/admissions/users/{userId}/flow`

Returns:
- List of steps
- Current step index
- Current task
- User status

---

### Get Current State

**GET** `/admissions/users/{userId}/current-step`

Returns:
- Current step
- Current task

---

### Execute Task

**PUT** `/admissions/users/{userId}/tasks/{taskName}`

Example request body (IQ test):

```json
{
  "testId": "test1",
  "score": 90,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

---

### Get User Status

**GET** `/admissions/users/{userId}/status`

Returns:

```
IN_PROGRESS / ACCEPTED / REJECTED
```

---

## Running Tests

Run all tests:

```bash
mvn test
```

Tests include:
- Core flow logic (AdmissionsFacade)
- Controller behavior
- Edge cases

---

## Writing Tests

Tests are located under:

```
src/test/java/...
```

Example:

~~~
String userId = facade.createUser();

facade.handleTask(userId, TaskName.PERSONAL_DETAILS, validPersonal());

assertEquals(StepName.IQ_TEST, facade.getProgress(userId).getCurrentStep());
~~~

---

## Quick Manual Test Flow

1. Create a user
2. Submit PERSONAL_DETAILS
3. Submit IQ_TEST
4. Complete interview (schedule + perform)
5. Continue until ACCEPTED

Try invalid cases:
- Executing tasks out of order
- Repeating a task
- Low IQ score (should result in REJECTED)

---

## Notes

- The system uses an in-memory repository (no database required)
- Validation is handled at the DTO level
- Errors are mapped to HTTP responses via a global exception handler

---

## Summary

This guide provides everything needed to:
- Run the application
- Test its behavior
- Interact with the API

No additional setup is required beyond Java and Maven.

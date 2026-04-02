# Admissions Flow Service

A Spring Boot service that models a multi-step admissions process.

The system supports creating users, tracking their progress through the admissions funnel, executing tasks through REST endpoints, and determining whether a user is still in progress, accepted, or rejected.

Before reviewing the code, it is recommended to read `docs/design.md`, which explains the main architectural decisions and the runtime flow model.

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

## 1. Architecture Overview

The project is organized around a small set of core components:

- **AdmissionsController** – exposes user-facing endpoints such as create user, get full flow, get current state, and get user status.
- **TaskController** – exposes a generic endpoint for completing any task in the admissions flow.
- **AdmissionsFacade** – contains the main orchestration logic: loading user progress, validating task execution, executing tasks, and advancing the user in the flow.
- **UserFlow / RuntimeStep** – represent the runtime admissions flow assigned to each user.
- **TaskFactory** – resolves task implementations by `TaskName`.
- **UserProgressRepository** – stores user state. In this project, it is implemented in memory.

A key design choice in this solution is that each user gets a personal runtime flow (`UserFlow`) instead of relying only on a single global static flow. This makes the system easier to extend for future product changes such as conditional or user-specific steps.

---

## 2. Project Structure

```text
src/main/java/com/masterschool/admissions
├── builder       # Manual composition root for tests / app wiring
├── config        # Spring configuration
├── controller    # REST controllers
├── domain        # Core domain objects and execution state
├── dto           # Request and response DTOs
├── exception     # Custom exceptions
├── facade        # Main orchestration layer
├── flow          # Flow-related enums / static configuration helpers
├── repository    # Persistence abstraction and in-memory implementation
├── runtime       # User-specific runtime flow model
├── service       # Supporting services such as request mapping
├── task          # Task implementations, enums, and factory
└── validations   # Validation helpers used by DTOs
```

---

## 3. Tech Stack

- Java 17
- Spring Boot
- Maven
- JUnit 5

---

## 4. Prerequisites

Make sure the following are installed:

- Java 17
- Maven 3.8+

Verify installation:

```bash
java -version
mvn -version
```

---

## 5. Clone and Build

```bash
git clone <your-repository-url>
cd admissions-flow-service
mvn clean install
```

If the build succeeds, the project is ready to run.

---

## 6. Run the Application

Start the server with:

```bash
mvn spring-boot:run
```

By default, the application runs on:

```text
http://localhost:8080
```

---

## 7. Main API Endpoints

Base path:

```text
/admissions
```

### 7.1 Create User

Creates a new user and returns a unique user ID.

```http
POST /admissions/users?email=test@mail.com
```

Example:

```bash
curl -X POST "http://localhost:8080/admissions/users?email=test@mail.com"
```

---

### 7.2 Get Full Flow

Returns the user’s runtime flow, current step index, current task, and overall status.

```http
GET /admissions/users/{userId}/flow
```

Example:

```bash
curl "http://localhost:8080/admissions/users/{userId}/flow"
```

---

### 7.3 Get Current State

Returns the user’s current step and current task.

```http
GET /admissions/users/{userId}/current-step
```

Example:

```bash
curl "http://localhost:8080/admissions/users/{userId}/current-step"
```

---

### 7.4 Complete a Task

Generic endpoint for completing any task in the flow.

```http
PUT /admissions/users/{userId}/tasks/{taskName}
```

Request body format:

```json
{
  "stepName": "IQ_TEST",
  "payload": {
    "testId": "test1",
    "score": 90,
    "timestamp": "2024-01-01T10:00:00Z"
  }
}
```

Example:

```bash
curl -X PUT "http://localhost:8080/admissions/users/{userId}/tasks/IQ_TEST" \
  -H "Content-Type: application/json" \
  -d '{
    "stepName": "IQ_TEST",
    "payload": {
      "testId": "test1",
      "score": 90,
      "timestamp": "2024-01-01T10:00:00Z"
    }
  }'
```

---

### 7.5 Get User Status

Returns the current user status.

```http
GET /admissions/users/{userId}/status
```

Possible values:

```text
IN_PROGRESS
ACCEPTED
REJECTED
```

Example:

```bash
curl "http://localhost:8080/admissions/users/{userId}/status"
```

---

## 8. Supported Admissions Flow

The current admissions flow contains the following steps:

1. Personal Details
2. IQ Test
3. Interview
    - Schedule Interview
    - Perform Interview
4. Sign Contract
    - Upload Identification
    - Sign Contract
5. Payment
6. Join Slack

A step is considered complete only when all of its tasks have been completed successfully.

Some tasks are pass/fail based on payload data, for example:

- `IQ_TEST` passes only if the score is greater than 75
- `PERFORM_INTERVIEW` passes only if the decision is `passed_interview`

If a failing task is submitted, the user is rejected and the flow stops.

---

## 9. Quick Manual Test

A simple end-to-end manual test:

### 9.1 Create a user

```bash
curl -X POST "http://localhost:8080/admissions/users?email=test@mail.com"
```

Save the returned user ID.

### 9.2 Submit personal details

```bash
curl -X PUT "http://localhost:8080/admissions/users/{userId}/tasks/PERSONAL_DETAILS" \
  -H "Content-Type: application/json" \
  -d '{
    "stepName": "PERSONAL_DETAILS",
    "payload": {
      "firstName": "Ohad",
      "lastName": "Daniel",
      "email": "test@mail.com",
      "timestamp": "2024-01-01T10:00:00Z"
    }
  }'
```

### 9.3 Submit IQ test with a passing score

```bash
curl -X PUT "http://localhost:8080/admissions/users/{userId}/tasks/IQ_TEST" \
  -H "Content-Type: application/json" \
  -d '{
    "stepName": "IQ_TEST",
    "payload": {
      "testId": "iq-1",
      "score": 90,
      "timestamp": "2024-01-01T10:05:00Z"
    }
  }'
```

### 9.4 Check flow / state / status

```bash
curl "http://localhost:8080/admissions/users/{userId}/flow"
curl "http://localhost:8080/admissions/users/{userId}/current-step"
curl "http://localhost:8080/admissions/users/{userId}/status"
```

### 9.5 Continue through the remaining tasks

- `SCHEDULE_INTERVIEW`
- `PERFORM_INTERVIEW`
- `UPLOAD_IDENTIFICATION`
- `SIGN_CONTRACT`
- `PAYMENT`
- `JOIN_SLACK`

At the end, the user status should become `ACCEPTED`.

---

## 10. Useful Invalid Scenarios to Try

You can also test a few edge cases manually:

- Submit a task out of order
- Submit the same task twice
- Submit `IQ_TEST` with a low score
- Submit `PERFORM_INTERVIEW` with a failing decision
- Try completing a task after the user is already accepted or rejected

These cases should result in an error or a rejection status, depending on the scenario.

---

## 11. Running Tests

Run all tests with:

```bash
mvn test
```

The current test suite covers:

- core flow progression
- controller behavior
- status transitions
- invalid task order
- repeated task execution
- rejection scenarios
- user state edge cases

---

## 12. Writing Additional Tests

Tests are located under:

```text
src/test/java/...
```

A useful pattern for writing tests is:

1. Create a user
2. Complete one or more tasks
3. Assert current step, status, or flow output

Good additional test cases include:

- invalid step-task combinations
- invalid payload scenarios
- runtime flow behavior
- controller-level edge cases

---

## 13. Notes

- The repository is in-memory, so all data is lost when the application stops.
- Validation is performed in the request DTOs using the validation helpers in the `validations` package.
- The service is intentionally focused on admissions flow execution logic; no database or frontend is required.
- The design now supports per-user runtime flow, making future product changes easier to implement.



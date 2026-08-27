# Course Enrollment System

Backend REST API for course enrollment — seat capacity, automatic waitlist, and grading.

Requirements and design live in [`docs/`](docs/): the [SRS](docs/Course-Enrollment-System-Requirements.pdf) defines what the system does, the [TDD](docs/Course-Enrollment-System-Technical-Design.pdf) defines how it is built and why each decision was made.

## Layout

```
enrollment/
├── src/main/java/com/example/enrollment/
│   ├── controller/  REST endpoints
│   ├── service/     business rules, transaction boundaries
│   ├── repository/  Spring Data JPA
│   ├── entity/      JPA entities — schema is generated from these
│   ├── dto/         request/response objects
│   ├── enums/       CourseStatus, EnrollmentStatus (+ transition rules)
│   ├── exception/   custom exceptions + GlobalExceptionHandler
│   └── common/      ApiResponse envelope
├── src/main/resources/application.yaml
├── src/test/        JUnit 5 + Mockito
└── docs/            SRS, TDD
```

Spring Boot 4 (Java 21), PostgreSQL 18, Maven. Layered — a layer calls only the one beneath it.

## Quickstart

Prereqs: Java 21, Maven 3.9+, PostgreSQL 16+.

```sh
# Empty database — do not create tables, Hibernate generates them
psql -U postgres -c "CREATE DATABASE course_enrollment;"

# Password is read from the environment, never from a file
export DB_PASSWORD=<your postgres password>    # Windows: $env:DB_PASSWORD="..."

mvn spring-boot:run
```

The service runs on `:8080`. `GET /api/courses` returning `200` confirms the app is up and the database is connected.

> **IntelliJ:** enable Settings → Build, Execution, Deployment → Compiler → Annotation Processors → *Enable annotation processing*, or Lombok-generated methods will not resolve.

## Modules

| Module | Purpose | Status |
|---|---|---|
| Course | Course master data, seat capacity, open/close | Code complete, untested |
| Student | Student master data | Not started |
| Enrollment | Capacity check, waitlist, automatic promotion, grading | Not started |

## API

All responses use the same envelope — `success`, `message`, `data`. Null fields are omitted.

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/courses` | Create a course → `201` |
| `GET` | `/api/courses` | List, paginated; `?status=OPEN` to filter |
| `GET` | `/api/courses/{id}` | Retrieve one |
| `PUT` | `/api/courses/{id}` | Update |
| `PATCH` | `/api/courses/{id}/close` | Close to further enrollment |
| `DELETE` | `/api/courses/{id}` | Delete |

Status codes: `400` validation, `404` not found, `409` conflict, `500` unhandled. Full endpoint list including planned routes is in [TDD §6.3](docs/Course-Enrollment-System-Technical-Design.pdf).

## Business rules

A full course does not reject the request — it queues it. When an enrolled student withdraws, the longest-waiting student is promoted automatically, in the same transaction.

| ID | Rule |
|---|---|
| BR-01 | One enrollment per student per course, in any status |
| BR-02 | At capacity, further requests are waitlisted, not rejected |
| BR-03 | On withdrawal, the longest-waiting student is promoted |
| BR-04 | A grade requires a `COMPLETED` enrollment |
| BR-05 | `DROPPED` and `COMPLETED` are terminal |
| BR-06 | No enrollment against a `CLOSED` course |

Enforcement points for each rule: [TDD §5.1](docs/Course-Enrollment-System-Technical-Design.pdf).

## Testing

```sh
mvn test
mvn clean test jacoco:report    # coverage → target/site/jacoco/index.html
```

Unit tests target the service layer, where the rules live. Verify failure paths, not just success — duplicate submission, missing resource, invalid input, invalid transition.

To verify the waitlist end to end: create a course with `capacity: 2`, enroll three students (the third should be `WAITLISTED`), drop the first, then check the third is now `ENROLLED`.

## Known gaps

> **No authentication.** Every endpoint is open. Do not expose outside a trusted network.

- `ddl-auto: update` adds columns but does not drop or rename — drop and recreate the database in development after a field is removed
- Range checks (`capacity > 0`, `semester` 1–8) are enforced by Bean Validation only, not by database `CHECK` constraints
- Deleting a referenced course or student returns `500` instead of `409`
- Capacity is checked in application code, so a concurrent request could theoretically overrun it by one; the composite unique constraint still prevents duplicates. Fix is a pessimistic lock on the course row

## Status

Foundations (schema, exception handling, response envelope), the Course module and the Student module are done. Enrollment is next; it carries the waitlist logic and is the core of the system.

| Module | Code | Tested | Report |
|---|---|---|---|
| M1 Course | Done | 31/31 PASS | [TER-CES-M1-001 v3.0](docs/) |
| M2 Student | Done | 28/28 PASS | [TER-CES-M2-001 v1.0](docs/) |
| M3 Enrollment, Grading, Roster | Not started | — | — |

## Owners

[Your Name] — `[your.email@company.com]` · `[#channel]` · `[Jira project]`

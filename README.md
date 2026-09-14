
## School of Sorcery

### Overview

The application is build as a Java(Gradle) + Spring Boot microservice that exposes a few endpoints.
They can be checked here once the app is running:
```
http://localhost:8080/swagger-ui/index.html
```

The entry point for the app is [App.java](src/main/java/com/liferay/App.java)

---

### Requirements

- Java 21
- Docker

---

### How to run locally

1. Start Docker daemon
2. Run `docker compose up` in the root of the project to generate a container with a Postgres DB.
3. Start the app via IDE or build it with `./gradlew clean build` and then run the jar: `java -jar build/libs/school-of-sorcery-1.0-SNAPSHOT.jar`

---

### Postman Collection

There is a folder called `postman-collection` that holds a collection with the REST calls for Postman.

---

### Open API

```
http://localhost:8080/api/v3/api-docs
```

---

### Architecture

The project is separated in **infrastructure** / **domain** / **application** layers.

Instead of Ports and Adapters naming convention, I always prefer to avoid using a suffix on interfaces
and use "Impl" for the classes implementing them.

Inbounds and outbounds packages help separating the code flow concern. Inbounds will hold the
code related to flow entering the app (like controllers, schedulers, etc...) whereas outbounds holds
the flow leaving the app (DB access, calls to external systems, etc...).

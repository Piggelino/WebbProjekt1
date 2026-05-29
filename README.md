# WebbProjekt1

Spring Boot project with a web page and OpenAPI docs for two API groups:

- `API 1`: Ticketmaster event endpoints (`/api/v1/events/...`)
- `API 2`: Placeholder endpoints for your future second API (`/api/v2/...`)

## Requirements

- Java 8+
- Maven 3.6+ (or Maven Wrapper)

## Ticketmaster API key setup

1. Copy the example key file:

```powershell
Copy-Item "src\main\resources\application-keys.properties.example" "src\main\resources\application-keys.properties"
```

2. Edit `src/main/resources/application-keys.properties` and set your key:

```properties
ticketmaster.api.key=YOUR_TICKETMASTER_API_KEY_HERE
ticketmaster.api.base-url=https://app.ticketmaster.com/discovery/v2
```

The app imports this file via `spring.config.import` in `src/main/resources/application.properties`.

## Run locally

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

App URL:

```text
http://localhost:8080
```

## API endpoints

### API 1 - Ticketmaster Events

- `GET /api/v1/events/search?keyword=concert&size=10`
- `GET /api/v1/events/search/city?city=Stockholm&size=10`
- `GET /api/v1/events/{eventId}`

Returned event fields include ID, name, coordinates (latitude/longitude), and date/time.

Events are cached in memory while the app is running, so the Ticketmaster API is only called when an event is not already stored.

### API 2 - Future API placeholder

- `GET /api/v2/example`
- `POST /api/v2/example`

## API documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Use Swagger UI to inspect both API groups and test endpoints.


# URL Shortner

This is a Spring Boot application to shorten a URL.

## Run with Docker (single command)

The project is dockerized and can be started with a single command using Docker Compose.

Prerequisites: Docker Engine 20+ and Docker Compose v2.

- Start (builds image if needed):
  - docker compose up -d --build
- Check logs:
  - docker compose logs -f
- Stop:
  - docker compose down

The app will be available at http://localhost:8080/v1/

For further use of the api Refer to the open-api doc in the file `openapi.yml`

Data persistence: The H2 file database is stored under ./data on your host machine and mounted into the container. This lets you keep data across restarts.

## Run without Docker (local)

- Build and run:
  - ./gradlew bootRun
  
OR

- Load this project in intelliJ and run the main function in the class UrlShortnerApplication.

## Possible Improvements

- Better logging
- CI/CD
- Use a better database
- Use a better URL shortening algorithm
- Sensitive properties in application.properties to be managed like secrets. For example, DB password
- Application properties to be managed in a better way to support multiple environments. For example, DB path and username
- Add UNIQUE constraint on fullUrl in schema.sql and make code changes to avoid creating multiple tinyUrls for same URL.
- Clean up unwanted files



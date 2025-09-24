# URL Shortner

This is a spring boot application to shorten a url.


## Possible Improvements

- Dockerfile
- Functional Tests
- CI/CD
- Use a better ID generation mechanism which can serve short URLs
- Add more tests
- Use a better database
- Use a better url shortening algorithm
- sensitive properties in application.properties to be managed like secrets. For example, db password
- Application properties to be managed in a better way to support multiple environments. For example, db path and username
- Add UNIQUE constraint on fulUrl in schema.sql and make code changes to avoid multiple tinyUrls for same url.



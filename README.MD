# FINANCE APP BACKEND
API for Finance App

## Building the application
    docker build --tag finance-app-server -f ./Dockerfile .

## Running the application
>Before running the application in Docker, you must run docker compose on `docker-compose.yml`.
This will run all the dependencies for this application to start.

Now you can run the docker image you have built

    docker run -e PORT=8081 -e DATABASE_URL=your.ip.address -e DB_USER=postgres -e DB_PASSWORD=postgres -e ISSUER_URI=localhost:8080 -e ISSUER_REALM=finance-app finance-app-server

To verify if the API is UP go to `http://localhost:8080/actuator/health`

## API Documentation
The Swagger of this application can be found at <br>
#### `http://localhost:8080/swagger-ui.html`


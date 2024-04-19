FROM openjdk:18
COPY . /usr/src/app
WORKDIR /usr/src/app

RUN chmod +x ./mvnw && ./mvnw clean install -DskipTests

COPY ./target/finance-app-backend-java-0.0.1-SNAPSHOT.jar ./app.jar

ENV PORT=$PORT
ENV DATABASE_URL=$DATABASE_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD
ENV ISSUER_URI=$ISSUER_URI
ENV JWK_SET_URI=$JWK_SET_URI
ENV SCHEMA=$SCHEMA

ENTRYPOINT ["java", "-jar", "./app.jar"]
EXPOSE $PORT

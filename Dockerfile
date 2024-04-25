FROM maven:3-openjdk-18 as mvn
COPY . ./usr/src/app
WORKDIR ./usr/src/app

RUN mvn clean install -DskipTests

FROM openjdk:18 as java
WORKDIR ./usr/src/app
COPY --from=mvn /usr/src/app/target/finance-app-backend-java-0.0.1-SNAPSHOT.jar ./app.jar

ENV PORT=$PORT
ENV DATABASE_URL=$DATABASE_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD
ENV ISSUER_URI=$ISSUER_URI
ENV JWK_SET_URI=$JWK_SET_URI
ENV SCHEMA=$SCHEMA

ENTRYPOINT ["java", "-jar", "./app.jar"]
EXPOSE $PORT

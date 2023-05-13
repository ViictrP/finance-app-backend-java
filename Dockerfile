FROM maven:3.8.3 AS maven
WORKDIR /usr/app
COPY . /usr/app
RUN mvn package

FROM openjdk:18
WORKDIR /usr/app

COPY --from=maven /usr/app/target/*.jar /usr/app.jar

ENV PORT=$PORT
ENV DATABASE_URL=$DATABASE_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD
ENV ISSUER_URI=$ISSUER_URI
ENV ISSUER_REALM=$ISSUER_HOST

ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE $PORT

FROM maven:3-eclipse-temurin-18-alpine
RUN mkdir /opt/app
WORKDIR /opt/app
COPY ./ .
RUN mvn install

ENV PORT=$PORT
ENV DATABASE_URL=$DATABASE_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD

CMD ["java", "-jar", "./config/target/financeapp-spring-1.0.0-SNAPSHOT.jar"]
EXPOSE 8080

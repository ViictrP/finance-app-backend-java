FROM openjdk:18-slim
RUN mkdir /opt/app
WORKDIR /opt/app
COPY ./ .
RUN ./mvnw install

ENV PORT=$PORT
ENV DATABASE_URL=$DATABASE_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD

CMD ["java", "-jar", "./config/target/financeapp-spring-1.0.0-SNAPSHOT.jar"]
EXPOSE 8080

FROM maven:3.9-eclipse-temurin-24

WORKDIR /app
COPY . .

RUN mvn package -DskipTests

EXPOSE 8080

CMD ["mvn", "jpro:run"]
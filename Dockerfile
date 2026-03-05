FROM maven:3.9-eclipse-temurin-24

RUN apt-get update && apt-get install -y \
    xvfb \
    libgtk-3-0 \
    libglib2.0-0 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

RUN mvn package -DskipTests

EXPOSE 8080

CMD Xvfb :99 -screen 0 1024x768x24 & export DISPLAY=:99 && mvn jpro:run
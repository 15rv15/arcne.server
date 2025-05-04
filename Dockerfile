FROM maven:3.9.6-eclipse-temurin-21 as build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/game-server-1.0-SNAPSHOT.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "app.jar"]

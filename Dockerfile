FROM openjdk:21-jdk-slim AS build
WORKDIR /BankExchange
COPY . .
RUN ./mvnw -DskipTests clean package

FROM openjdk:21-jdk-slim
WORKDIR /BankExchange
COPY --from=build /BankExchange/target/*.jar ./BankExchange.jar
ENTRYPOINT ["java", "-jar", "BankExchange.jar"]

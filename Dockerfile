# Estágio 1: Build da aplicação com Maven
FROM maven:3.8.5-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
# Baixa as dependências (cacheável)
RUN mvn dependency:go-offline
COPY src ./src
# Compila, testa e empacota a aplicação
RUN mvn package -DskipTests

# Estágio 2: Criação da imagem final, menor
FROM openjdk:11-jre-slim
WORKDIR /app
# Copia o JAR do estágio de build para a imagem final
COPY --from=build /app/target/my-java-app-1.0-SNAPSHOT.jar .
EXPOSE 8080
# Comando para rodar a aplicação
CMD ["java", "-jar", "my-java-app-1.0-SNAPSHOT.jar"]

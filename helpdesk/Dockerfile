# Estágio 1: Build da Aplicação
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Define o diretório de trabalho dentro do container de build
WORKDIR /app

# Copia o pom.xml e faz o download das dependências
COPY pom.xml .
RUN mvn dependency:go-offline package -DskipTests

# Copia todo o código-fonte restante e faz o build final
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Criação da Imagem Executável
# Usa uma base Java JRE leve (ideal para containers)
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Define o nome exato do JAR para copiar
ARG JAR_FILE=target/helpdesk-0.0.1-SNAPSHOT.jar 

# Copia o JAR do estágio de build para o estágio final, renomeando para 'app.jar'
COPY --from=build /app/${JAR_FILE} app.jar

# Define o comando de inicialização
# O Spring Boot vai usar a variável de ambiente 'PORT' injetada pelo Render
ENTRYPOINT ["java", "-jar", "app.jar"]
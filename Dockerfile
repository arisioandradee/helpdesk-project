# Usando Java 17
FROM eclipse-temurin:17-jdk-jammy

# Diretório da aplicação
WORKDIR /app

# Copiar arquivos do projeto
COPY . .

# Build do Maven
RUN ./mvnw clean package -DskipTests

# Expor a porta
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "target/helpdesk-0.0.1-SNAPSHOT.jar"]

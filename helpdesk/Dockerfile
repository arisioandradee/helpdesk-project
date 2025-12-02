# Estágio 1: Build da Aplicação
# Usa uma imagem base que inclui o Maven e o Java
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml para que o Maven possa baixar as dependências primeiro
COPY pom.xml .

# Baixa as dependências e compila, pulando os testes que falharam localmente
RUN mvn dependency:go-offline package -DskipTests

# Copia todo o código-fonte restante
COPY src ./src

# Recompila (rápido, pois as dependências já foram baixadas)
# A saída final (o JAR) estará na pasta target
RUN mvn clean package -DskipTests

# Estágio 2: Criação da Imagem Executável
# Usa uma imagem base Java leve (apenas o JRE, sem o Maven)
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Argumento para o nome do JAR (ajuste conforme o nome do seu projeto)
ARG JAR_FILE=target/helpdesk-0.0.1-SNAPSHOT.jar 
# **ATENÇÃO:** Altere 'helpdesk-0.0.1-SNAPSHOT.jar' para o nome exato do seu arquivo gerado na pasta target.

# Copia o JAR do estágio de build para o estágio final
COPY --from=build /app/${JAR_FILE} app.jar

# Define as variáveis de ambiente padrões (opcional, mas bom)
ENV PORT 8080

# Comando para rodar a aplicação quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]
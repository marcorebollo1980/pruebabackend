# Usa una imagen base de OpenJDK para construir la aplicación
FROM eclipse-temurin:17-jdk-jammy as builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Maven (pom.xml)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn/
# COPY mvnw.cmd . # Descomenta si es necesario para tu setup específico

# Da permisos de ejecución al script mvnw
RUN chmod +x ./mvnw

# Copia el código fuente de la aplicación
COPY src ./src

# Construye la aplicación Spring Boot. Esto generará un JAR ejecutable.
# Se usa `-Dmaven.test.skip=true` para saltar las pruebas durante la construcción del Dockerfile
RUN ./mvnw clean install -DskipTests

# Etapa de ejecución: Usa una imagen JRE más pequeña para la aplicación final
FROM eclipse-temurin:17-jre-jammy

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR construido de la etapa 'builder'
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto en el que la aplicación Spring Boot se ejecutará
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]


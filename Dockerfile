FROM gradle:8.8-jdk17

# Copia todo el contenido del proyecto al contenedor
COPY . .

# Construye el proyecto utilizando Gradle
RUN gradle build

# Expone el puerto 8080 para que el contenedor escuche
EXPOSE 8080

# Comando de entrada para ejecutar el archivo .jar generado
ENTRYPOINT ["java", "-jar", "build/libs/homebanking-0.0.1-SNAPSHOT.jar"]

FROM gradle:8.8-jdk17

COPY . .

RUN gradle build

EXPOSE 8080

ENTRYPOINT [" java","-jar","build/libs/homebanking-0.0.1-SNAPSHOT.jar"]
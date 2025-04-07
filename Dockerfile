FROM openjdk:17

COPY target/Shopping-app.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Shopping-app.jar"]
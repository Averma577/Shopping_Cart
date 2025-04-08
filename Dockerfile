FROM openjdk:17

COPY target/Shopping-cart.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Shopping-cart.jar"]
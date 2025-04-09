FROM openjdk:17

COPY target/shopping_cart.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "shopping_cart.jar"]
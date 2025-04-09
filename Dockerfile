FROM openjdk:17

COPY target/shopping_cart.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "shopping_cart.jar"]
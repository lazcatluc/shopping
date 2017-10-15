FROM java:alpine
COPY target/shopping.jar /
ENTRYPOINT ["java", "-jar", "/shopping.jar"]

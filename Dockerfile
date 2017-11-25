FROM java:alpine
COPY target/shopping.jar /
ENTRYPOINT ["java", "-Dspring.datasource.url=jdbc:mysql://mysql:3306/catalin_prod", "-Dspring.jpa.hibernate.ddl-auto=update", "-Dspring.jpa.properties.hibernate.show_sql=false", "-jar", "/shopping.jar"]

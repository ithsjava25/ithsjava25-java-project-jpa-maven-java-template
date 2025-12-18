FROM eclipse-temurin:21-jdk

COPY src/main/java/org/example/App.java /App.java

ENTRYPOINT ["java", "/App.java"]


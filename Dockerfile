FROM openjdk:11
ARG JAR_FILE
COPY target/myf.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

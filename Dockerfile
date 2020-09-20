FROM adoptopenjdk/openjdk11:alpine-slim
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar","/app.jar"]
#ENTRYPOINT ["java","-jar","/app.jar"]
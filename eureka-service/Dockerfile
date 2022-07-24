FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.11_9-slim as builder
WORKDIR application
EXPOSE 8761
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.11_9-slim
WORKDIR application
COPY --from=builder application/dependencies ./
COPY --from=builder application/spring-boot-loader ./
COPY --from=builder application/snapshot-dependencies ./
#COPY --from=builder application/resources ./
COPY --from=builder application/application ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

#source: https://blog.tratif.com/2020/05/20/spring-tips-2-layered-jars-with-spring-boot-2-3-0/
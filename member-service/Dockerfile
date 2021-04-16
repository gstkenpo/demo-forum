FROM adoptopenjdk:11-jre-openj9 as builder
WORKDIR application
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM adoptopenjdk:11-jre-openj9
WORKDIR application
COPY --from=builder application/dependencies ./
COPY --from=builder application/spring-boot-loader ./
COPY --from=builder application/snapshot-dependencies ./
#COPY --from=builder application/resources ./
COPY --from=builder application/application ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

#source: https://blog.tratif.com/2020/05/20/spring-tips-2-layered-jars-with-spring-boot-2-3-0/
FROM  docker.kkt.mcb.ru/fabric8/java-alpine-openjdk11-jre:latest
COPY finance-accounting-impl/target/*.jar /app.jar
WORKDIR /
ENV spring.profiles.active=local
ENV TZ=Europe/Moscow
#переопределяйте JAVA_OPTS при запуске контейнера docker run -e JAVA_OPTS="-param1 -param2 ..."
ENV JAVA_OPTS=""
ENV JAVA_RAM_OPTS="-XX:InitialRAMPercentage=80.0 -XX:MaxRAMPercentage=80.0"
ENTRYPOINT exec java $JAVA_OPTS $JAVA_RAM_OPTS -jar /app.jar

FROM openjdk:8
VOLUME /tmp
EXPOSE 8080
ADD ./build/libs/event-client-service-0.0.1.jar app.jar
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm"
ENV DEBUG_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n"
ENTRYPOINT [ "sh", "-c", "java $DEBUG_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
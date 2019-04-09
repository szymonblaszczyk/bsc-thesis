FROM openjdk:8
VOLUME /tmp
EXPOSE 8080
ADD ./build/libs/gateway-service-0.0.1.jar app.jar
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm"
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
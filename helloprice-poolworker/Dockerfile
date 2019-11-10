FROM java:8
WORKDIR /
ADD podo-helloprice-poolworker.jar podo-helloprice-poolworker.jar
EXPOSE 8443
RUN echo "Asia/Seoul" > /etc/timezone
CMD ["java", "-jar", "-Dspring.profiles.active=deploy", "-Xms2048m", "-Xmx2048m", "-XX:NewSize=1024m", "-XX:MaxNewSize=1024m", "-XX:MetaspaceSize=100M", "podo-helloprice-poolworker.jar"]

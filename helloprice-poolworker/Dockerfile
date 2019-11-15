FROM java:8
WORKDIR /
ADD podo-helloprice-poolworker.jar podo-helloprice-poolworker.jar
EXPOSE 8443
RUN echo "Asia/Seoul" > /etc/timezone
CMD ["java", "-jar", "-Dspring.profiles.active=deploy", "-Xms512m", "-Xmx512m", "-XX:NewSize=256m", "-XX:MaxNewSize=256m", "-XX:MetaspaceSize=100M", "podo-helloprice-poolworker.jar"]

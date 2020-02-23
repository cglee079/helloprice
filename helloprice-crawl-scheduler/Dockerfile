FROM java:8
WORKDIR /
ADD helloprice-crawl-scheduler.jar helloprice-crawl-scheduler.jar
EXPOSE 8443
RUN echo "Asia/Seoul" > /etc/timezone
CMD ["java", "-jar", "-Dspring.profiles.active=deploy", "-Xms512m", "-Xmx512m", "-XX:NewSize=256m", "-XX:MaxNewSize=256m", "-XX:MetaspaceSize=50M", "helloprice-crawl-scheduler.jar"]

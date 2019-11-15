FROM java:8
WORKDIR /
ADD podo-helloprice-telegram.jar podo-helloprice-telegram.jar
EXPOSE 8443
RUN echo "Asia/Seoul" > /etc/timezone
CMD ["java", "-jar", "-Dspring.profiles.active=deploy", "-Xms1024m", "-Xmx1024m", "-XX:NewSize=512m", "-XX:MaxNewSize=512m", "-XX:MetaspaceSize=100M", "podo-helloprice-telegram.jar"]
